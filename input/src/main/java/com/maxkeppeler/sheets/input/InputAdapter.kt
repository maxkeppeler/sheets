/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.maxkeppeler.sheets.input

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.internal.ViewUtils
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.core.views.SheetContent
import com.maxkeppeler.sheets.input.databinding.*
import com.maxkeppeler.sheets.input.type.*


internal typealias InputAdapterChangeListener = () -> Unit

internal class InputAdapter(
    private val ctx: Context,
    private val input: MutableList<Input>,
    private val listener: InputAdapterChangeListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EDIT_TEXT = 0
        private const val VIEW_TYPE_CHECK_BOX = 1
        private const val VIEW_TYPE_RADIO_BUTTONS = 2
        private const val VIEW_TYPE_SPINNER = 3
        private const val VIEW_TYPE_SWITCH = 4
    }

    private val primaryColor = getPrimaryColor(ctx)
    private val iconsColor = getIconColor(ctx)
    private val highlightColor = getHighlightColor(ctx)
    private val textColor = getTextColor(ctx)

    override fun getItemViewType(i: Int): Int = when (input[i]) {
        is InputEditText -> VIEW_TYPE_EDIT_TEXT
        is InputSwitch -> VIEW_TYPE_SWITCH
        is InputCheckBox -> VIEW_TYPE_CHECK_BOX
        is InputRadioButtons -> VIEW_TYPE_RADIO_BUTTONS
        is InputSpinner -> VIEW_TYPE_SPINNER
        else -> throw IllegalStateException("No ViewType for this Input.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder =
        when (type) {
            VIEW_TYPE_EDIT_TEXT -> EditTextItem(
                SheetsInputEditTextItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_CHECK_BOX -> CheckBoxItem(
                SheetsInputCheckBoxItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_SWITCH -> SwitchItem(
                SheetsInputSwitchItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_RADIO_BUTTONS -> RadioButtonsItem(
                SheetsInputRadioButtonsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_SPINNER -> SpinnerItem(
                SheetsInputSpinnerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("No ViewHolder for this ViewType.")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val input = input[i]
        when {
            holder is EditTextItem && input is InputEditText -> holder.binding.buildEditText(input)
            holder is CheckBoxItem
                    && input is InputCheckBox -> {
                holder.binding.buildCheckBox(input)
            }
            holder is SwitchItem
                    && input is InputSwitch -> {
                holder.binding.buildSwitch(input)
            }
            holder is RadioButtonsItem
                    && input is InputRadioButtons -> {
                holder.binding.buildRadioButtons(input)
            }
            holder is SpinnerItem
                    && input is InputSpinner -> {
                holder.binding.buildSpinner(input)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun SheetsInputEditTextItemBinding.buildEditText(input: InputEditText) {

        setupGeneralInputInfo(input, label, null)

        with(textInput) {

            setText(input.value)

            input.isPasswordVisible?.let {
                transformationMethod = if (it) null else PasswordTransformationMethod()
            }

            input.inputType?.let { inputType = it }
            input.maxLines?.let { maxLines = it }
            input.inputFilter?.let { filters = arrayOf(it) }

            isVerticalScrollBarEnabled = true

            setOnTouchListener { view, motionEvent ->
                if (motionEvent.action != 0 && MotionEvent.ACTION_UP != 0 || MotionEvent.ACTION_DOWN != 0) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    false
                } else false
            }

            addTextWatcher { text ->
                input.value = text
                listener.invoke()
            }
        }

        with(textInputLayout) {

            val hintText = input.hintRes?.let { ctx.getString(it) } ?: input.hint
            hintText?.let { hint = it }

            input.endIconMode?.let { endIconMode = it }
            input.isEndIconActivated?.let { setEndIconActivated(it) }
            (input.startIconDrawableRes ?: input.drawableRes)?.let { setStartIconDrawable(it) }
            input.errorIconDrawableRes?.let { setErrorIconDrawable(it) }
            input.isEndIconActivated?.let { setEndIconActivated(it) }
            setStartIconTintList(ColorStateList.valueOf(primaryColor))

            input.validationResultListener { result ->
                if (!result.valid) {
                    error = result.errorText
                }
                isErrorEnabled = !result.valid
            }
        }
    }

    private fun SheetsInputSwitchItemBinding.buildSwitch(input: InputSwitch) {

        setupGeneralInputInfo(input, label, icon)

        switchButton.isChecked = input.value

        val checkBoxText = input.textRes?.let { ctx.getString(it) } ?: input.text
        switchButton.text = checkBoxText
        switchButton.setTextColor(textColor)
        switchButton.buttonTintList = ColorStateList.valueOf(primaryColor)
        switchButton.setOnCheckedChangeListener { _, checked ->
            input.value = checked
            listener.invoke()
        }
    }

    private fun SheetsInputCheckBoxItemBinding.buildCheckBox(input: InputCheckBox) {

        setupGeneralInputInfo(input, label, icon)

        checkBox.isChecked = input.value

        val checkBoxText = input.textRes?.let { ctx.getString(it) } ?: input.text
        checkBox.text = checkBoxText
        checkBox.setTextColor(textColor)
        checkBox.buttonTintList = ColorStateList.valueOf(primaryColor)
        checkBox.setOnCheckedChangeListener { _, checked ->
            input.value = checked
            listener.invoke()
        }
    }

    private fun SheetsInputRadioButtonsItemBinding.buildRadioButtons(input: InputRadioButtons) {

        setupGeneralInputInfo(input, label)

        input.radioButtonOptions?.forEachIndexed { index, radioButtonText ->
            val button = AppCompatRadioButton(ctx).apply {
                setTextAppearance(ctx, android.R.style.TextAppearance_Material_Body2)
                setTextColor(textColor)
                buttonTintList = ColorStateList.valueOf(primaryColor)
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(16.toDp(), 0, 0, 0)
                text = radioButtonText
                id = index
            }
            radioGroup.addView(button)
        }

        input.value?.let { radioGroup.check(it) }
        radioGroup.setOnCheckedChangeListener { _, idIndex ->
            input.value = idIndex
            listener.invoke()
        }
    }

    private fun SheetsInputSpinnerItemBinding.buildSpinner(input: InputSpinner) {

        setupGeneralInputInfo(input, label, icon)

        val spinnerOptions = input.spinnerOptions

        if (input.value == null) {
            val spinnerNoSelectionText =
                input.textRes?.let { ctx.getString(it) } ?: input.noSelectionText
            spinnerOptions?.add(spinnerNoSelectionText ?: ctx.getString(R.string.select))
        }

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            ctx,
            android.R.layout.simple_spinner_dropdown_item, spinnerOptions ?: mutableListOf()
        ) {
            override fun getCount(): Int =
                super.getCount().takeIf { it > 0 }?.minus(1) ?: super.getCount()
        }
        icon.setColorFilter(primaryColor)
        spinner.adapter = adapter
        val selectionIndex = input.value ?: spinnerOptions?.lastIndex ?: 0
        spinner.setSelection(selectionIndex)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(aV: AdapterView<*>?, v: View, i: Int, id: Long) {
                (aV?.getChildAt(0) as TextView).setTextColor(textColor)
                if (i == selectionIndex) return
                input.value = i
                listener.invoke()
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) = Unit
        }
    }

    private fun setupGeneralInputInfo(
        input: Input,
        label: SheetContent? = null,
        icon: ImageView? = null,
    ) {

        label?.let {
            val labelText = input.labelRes?.let { ctx.getString(it) } ?: input.label
            labelText?.let {
                label.text = it.takeUnless { input.required } ?: it.plus(" *")
                label.visibility = View.VISIBLE
            }
        }

        icon?.let {
            input.drawableRes?.let { res ->
                icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
                icon.setColorFilter(iconsColor)
                icon.visibility = View.VISIBLE
            } ?: kotlin.run { icon.visibility = View.GONE }
        }
    }

    override fun getItemCount(): Int = input.size

    internal inner class EditTextItem(val binding: SheetsInputEditTextItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class SwitchItem(val binding: SheetsInputSwitchItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class CheckBoxItem(val binding: SheetsInputCheckBoxItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class RadioButtonsItem(val binding: SheetsInputRadioButtonsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class SpinnerItem(val binding: SheetsInputSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

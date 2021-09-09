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
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.core.utils.getIconColor
import com.maxkeppeler.sheets.core.utils.getPrimaryColor
import com.maxkeppeler.sheets.core.utils.getTextColor
import com.maxkeppeler.sheets.core.utils.toDp
import com.maxkeppeler.sheets.core.views.SheetsContent
import com.maxkeppeler.sheets.input.databinding.*
import com.maxkeppeler.sheets.input.type.*
import com.maxkeppeler.sheets.input.type.spinner.InputSpinner
import com.maxkeppeler.sheets.input.type.spinner.SpinnerAdapter
import com.maxkeppeler.sheets.input.type.spinner.SpinnerOption

internal typealias InputAdapterChangeListener = () -> Unit

internal class InputAdapter(
    private val ctx: Context,
    private val input: MutableList<Input>,
    private val listener: InputAdapterChangeListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EDIT_TEXT = 10
        private const val VIEW_TYPE_CHECK_BOX = 11
        private const val VIEW_TYPE_SWITCH = 12
        private const val VIEW_TYPE_SPINNER = 13
        private const val VIEW_TYPE_RADIO_BUTTONS = 14
        private const val VIEW_TYPE_BUTTON_TOGGLE_GROUP = 15
        private const val VIEW_TYPE_CUSTOM = 40
        private const val VIEW_TYPE_SEPARATOR = 50
    }

    private val inputViews = mutableMapOf<String, View>()
    private val primaryColor = getPrimaryColor(ctx)
    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)

    override fun getItemViewType(i: Int): Int = when (input[i]) {
        is InputEditText -> VIEW_TYPE_EDIT_TEXT
        is InputCheckBox -> VIEW_TYPE_CHECK_BOX
        is InputSwitch -> VIEW_TYPE_SWITCH
        is InputSpinner -> VIEW_TYPE_SPINNER
        is InputRadioButtons -> VIEW_TYPE_RADIO_BUTTONS
        is InputButtonToggleGroup -> VIEW_TYPE_BUTTON_TOGGLE_GROUP
        is InputCustom -> VIEW_TYPE_CUSTOM
        is InputSeparator -> VIEW_TYPE_SEPARATOR
        else -> throw IllegalStateException("No ViewType for this Input.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
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
            VIEW_TYPE_SPINNER -> SpinnerItem(
                SheetsInputSpinnerItemBinding.inflate(
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
            VIEW_TYPE_BUTTON_TOGGLE_GROUP -> ButtonToggleGroupItem(
                SheetsInputButtonToggleGroupItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_CUSTOM -> CustomItem(
                SheetsInputCustomItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_SEPARATOR -> SeparatorItem(
                SheetsInputSeparatorItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("No ViewHolder for this ViewType.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val input = input[i]
        val key = input.getKeyOrIndex(i)
        inputViews[key] = holder.itemView
        displayInput(key, input.visible)

        when {
            holder is EditTextItem && input is InputEditText -> {
                holder.binding.buildEditText(input)
            }
            holder is CheckBoxItem
                    && input is InputCheckBox -> {
                holder.binding.buildCheckBox(input)
            }
            holder is SwitchItem
                    && input is InputSwitch -> {
                holder.binding.buildSwitch(input)
            }
            holder is SpinnerItem
                    && input is InputSpinner -> {
                holder.binding.buildSpinner(input)
            }
            holder is RadioButtonsItem
                    && input is InputRadioButtons -> {
                holder.binding.buildRadioButtons(input)
            }
            holder is ButtonToggleGroupItem
                    && input is InputButtonToggleGroup -> {
                holder.binding.buildButtonToggleGroup(input)
            }
            holder is CustomItem
                    && input is InputCustom -> {
                holder.binding.buildCustom(input)
            }
            holder is SeparatorItem
                    && input is InputSeparator -> {
                val isHeader = i == 0
                holder.binding.buildSeparator(isHeader, input)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun SheetsInputEditTextItemBinding.buildEditText(input: InputEditText) {

        setupGeneralInputInfo(input, label, content, null)

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
            hintText?.let { hint = it.takeUnless { input.required && input.label == null } ?: it.plus(" *") }

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

    private fun SheetsInputCheckBoxItemBinding.buildCheckBox(input: InputCheckBox) {

        setupGeneralInputInfo(input, label, content, icon)

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

    private fun SheetsInputSwitchItemBinding.buildSwitch(input: InputSwitch) {

        setupGeneralInputInfo(input, label, content, icon)

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

    private fun SheetsInputSpinnerItemBinding.buildSpinner(input: InputSpinner) {

        setupGeneralInputInfo(input, label, content, icon)

        val spinnerOptions = mutableListOf<SpinnerOption>().apply { input.spinnerOptions?.let { addAll(it) } }

        if (input.value == null) {
            val noSelectionOption =
                input.textRes?.let { SpinnerOption(it) }
                    ?: input.noSelectionText?.let { SpinnerOption(it) }
                    ?: SpinnerOption(R.string.sheets_select)
            spinnerOptions.add(noSelectionOption)
        }

        val adapter: SpinnerAdapter = object : SpinnerAdapter(ctx, spinnerOptions) {
            override fun getCount(): Int =
                super.getCount().takeIf { spinnerOptions.size != input.spinnerOptions?.size }
                    ?.minus(1) ?: super.getCount()
        }
        icon.setColorFilter(primaryColor)
        spinner.adapter = adapter
        val selectionIndex = input.value ?: spinnerOptions.lastIndex
        spinner.setSelection(selectionIndex)
        spinner.tag = true // signal init setup selection
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(aV: AdapterView<*>?, v: View, i: Int, id: Long) {
                ((aV?.getChildAt(0) as ConstraintLayout).getViewById(R.id.text) as TextView).setTextColor(
                    textColor
                )
                if (spinner.tag == true) {
                    spinner.tag = false
                } else {
                    input.value = i
                    listener.invoke()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) = Unit
        }
    }

    private fun SheetsInputRadioButtonsItemBinding.buildRadioButtons(input: InputRadioButtons) {

        setupGeneralInputInfo(input, label, content)

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

    private fun SheetsInputButtonToggleGroupItemBinding.buildButtonToggleGroup(input: InputButtonToggleGroup) {

        setupGeneralInputInfo(input, label, content, icon)

        input.value?.let { buttonToggleGroup.selected(it) }

        buttonToggleGroup.options(input.toggleButtonOptions?.toList() ?: listOf())
        buttonToggleGroup.listener { index ->
            input.value = index
            listener.invoke()
        }
    }

    private fun SheetsInputCustomItemBinding.buildCustom(input: InputCustom) {

        if (!input.fullView) {
            setupGeneralInputInfo(input, label, content, icon)
        } else {
            (container.layoutParams as ConstraintLayout.LayoutParams).apply {
                setMargins(0, 0, 0, 0)
                goneStartMargin = 0
            }
        }

        val resolvedView = input.viewRes?.let { LayoutInflater.from(ctx).inflate(it, null, false) }
        val view = input.view ?: resolvedView
        view?.let {
            container.addView(it)
            input.viewListener?.invoke(it)
        }
        input.dataChangeListener = { listener.invoke() }
    }

    private fun SheetsInputSeparatorItemBinding.buildSeparator(
        header: Boolean,
        input: InputSeparator,
    ) {

        setupGeneralInputInfo(input, label, content, icon)

        val paddingTop = if (header) 0.toDp() else 16.toDp()
        root.setPadding(root.paddingLeft, paddingTop, root.paddingRight, root.paddingBottom)

        if (header) {
            divider.visibility = View.GONE
        }

        input.displayDivider?.let { displayDivider ->
            divider.visibility = if (displayDivider) View.VISIBLE else View.GONE
        }
    }

    private fun setupGeneralInputInfo(
        input: Input,
        label: SheetsContent,
        content: SheetsContent,
        icon: ImageView? = null,
    ) {

        val labelText = input.labelRes?.let { ctx.getString(it) } ?: input.label
        labelText?.let {
            label.text = it.takeUnless { input.required } ?: it.plus(" *")
            label.visibility = View.VISIBLE
        }

        val contentText = input.contentRes?.let { ctx.getString(it) } ?: input.content
        contentText?.let {
            content.text = it
            content.visibility = View.VISIBLE
        }

        icon?.let {
            input.drawableRes?.let { res ->
                icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
                icon.setColorFilter(iconsColor)
                icon.visibility = View.VISIBLE
            } ?: kotlin.run { icon.visibility = View.GONE }
        }
    }

    fun displayInput(key: String, visible: Boolean) {
        val view = inputViews[key] ?: return
        (view.layoutParams as RecyclerView.LayoutParams).apply {
            height = if (visible) RecyclerView.LayoutParams.WRAP_CONTENT else 0
            view.requestLayout()
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

    internal inner class ButtonToggleGroupItem(val binding: SheetsInputButtonToggleGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class CustomItem(val binding: SheetsInputCustomItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal inner class SeparatorItem(val binding: SheetsInputSeparatorItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

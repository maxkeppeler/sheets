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

package com.maxkeppeler.bottomsheets.input

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.bottomsheets.core.utils.*
import com.maxkeppeler.bottomsheets.core.views.BottomSheetContent
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputCheckBoxItemBinding
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputEditTextItemBinding
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputRadioButtonsItemBinding
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputSpinnerItemBinding
import com.maxkeppeler.bottomsheets.input.type.*

typealias InputAdapterChangeListener = () -> Unit

class InputAdapter(
    private val ctx: Context,
    private val input: MutableList<Input>,
    private val listener: InputAdapterChangeListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EDIT_TEXT = 0
        private const val VIEW_TYPE_CHECK_BOX = 1
        private const val VIEW_TYPE_RADIO_BUTTONS = 2
        private const val VIEW_TYPE_SPINNER = 3
    }

    private val primaryColor = getPrimaryColor(ctx)
    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)

    override fun getItemViewType(i: Int): Int = when (input[i]) {
        is InputEditText -> VIEW_TYPE_EDIT_TEXT
        is InputCheckBox -> VIEW_TYPE_CHECK_BOX
        is InputRadioButtons -> VIEW_TYPE_RADIO_BUTTONS
        is InputSpinner -> VIEW_TYPE_SPINNER
        else -> throw IllegalStateException("No ViewType for this Input.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            VIEW_TYPE_EDIT_TEXT -> {
                EditTextViewHolder(
                    BottomSheetsInputEditTextItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_CHECK_BOX -> {
                CheckBoxViewHolder(
                    BottomSheetsInputCheckBoxItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_RADIO_BUTTONS -> {
                RadioButtonsViewHolder(
                    BottomSheetsInputRadioButtonsItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_SPINNER -> {
                SpinnerViewHolder(
                    BottomSheetsInputSpinnerItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalStateException("No ViewHolder for this ViewType.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val input = input[i]
        when {
            holder is EditTextViewHolder && input is InputEditText -> {
                holder.binding.buildEditText(input)
            }
            holder is CheckBoxViewHolder
                    && input is InputCheckBox -> {
                holder.binding.buildCheckBox(input)
            }
            holder is RadioButtonsViewHolder
                    && input is InputRadioButtons -> {
                holder.binding.buildRadioButtons(input)
            }
            holder is SpinnerViewHolder
                    && input is InputSpinner -> {
                holder.binding.buildSpinner(input)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun BottomSheetsInputEditTextItemBinding.buildEditText(input: InputEditText) {

        setupGeneralInputInfo(input, label, icon)

        val hintText = input.hintRes?.let { ctx.getString(it) } ?: input.hint
        hintText?.let { text.hint = it }

        val valueText = input.defaultTextRes?.let { ctx.getString(it) } ?: input.defaultText
        valueText?.let { text.setText(it) }

        text.inputType = input.inputType
        input.inputFilter?.let { text.filters = arrayOf(it) }

        icon.setColorFilter(primaryColor)
        text.setTextColor(textColor)
        text.setHintTextColor(textColor)
        text.isVerticalScrollBarEnabled = true
        text.overScrollMode = View.OVER_SCROLL_ALWAYS
        text.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
        text.movementMethod = ScrollingMovementMethod.getInstance()

        text.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if (motionEvent.action and MotionEvent.ACTION_UP != 0 && motionEvent.actionMasked and MotionEvent.ACTION_UP != 0) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        text.addTextWatcher { text ->
            input.value = text
            listener.invoke()
        }
    }

    private fun BottomSheetsInputCheckBoxItemBinding.buildCheckBox(input: InputCheckBox) {

        setupGeneralInputInfo(input, label, icon)

        checkBox.isChecked = input.default

        val checkBoxText = input.textRes?.let { ctx.getString(it) } ?: input.text
        checkBox.text = checkBoxText
        checkBox.setTextColor(textColor)
        checkBox.buttonTintList = ColorStateList.valueOf(primaryColor)
        checkBox.setOnCheckedChangeListener { _, checked ->
            input.value = checked
            listener.invoke()
        }
    }

    private fun BottomSheetsInputRadioButtonsItemBinding.buildRadioButtons(input: InputRadioButtons) {

        setupGeneralInputInfo(input, label)

        input.radioButtonOptions?.forEachIndexed { index, radioButtonText ->
            val button = AppCompatRadioButton(ctx).apply {
                setTextAppearance(ctx, android.R.style.TextAppearance_Material_Body2)
                setTextColor(textColor)
                buttonTintList = ColorStateList.valueOf(primaryColor)
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8.toDp(), 0, 8.toDp(), 0)
                }
                setPadding(90.toDp(), 0, 0, 0)
                text = radioButtonText
                id = index
                isSelected = index == input.selectedIndex
            }
            radioGroup.addView(button)
        }

        radioGroup.setOnCheckedChangeListener { _, idIndex ->
            input.value = idIndex
            listener.invoke()
        }
    }

    private fun BottomSheetsInputSpinnerItemBinding.buildSpinner(input: InputSpinner) {

        setupGeneralInputInfo(input, label, icon)

        if (input.selectedIndex == null) {
            val spinnerNoSelectionText = input.textRes?.let { ctx.getString(it) } ?: input.text
            input.spinnerOptions?.add(spinnerNoSelectionText ?: ctx.getString(R.string.select))
        }


        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            ctx,
            android.R.layout.simple_spinner_dropdown_item, input.spinnerOptions ?: mutableListOf()
        ) {
            override fun getCount(): Int =
                super.getCount().takeIf { it > 0 }?.minus(1) ?: super.getCount()
        }
        icon.setColorFilter(primaryColor)
        spinner.adapter = adapter
        val selectionIndex = input.selectedIndex ?: input.spinnerOptions?.lastIndex ?: 0
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
        label: BottomSheetContent,
        icon: ImageView? = null
    ) {

        val labelText = input.labelRes?.let { ctx.getString(it) } ?: input.label
        labelText?.let {
            label.text = it.takeUnless { input.required } ?: it.plus(" *")
            label.visibility = View.VISIBLE
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

    inner class EditTextViewHolder(val binding: BottomSheetsInputEditTextItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CheckBoxViewHolder(val binding: BottomSheetsInputCheckBoxItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class RadioButtonsViewHolder(val binding: BottomSheetsInputRadioButtonsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class SpinnerViewHolder(val binding: BottomSheetsInputSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

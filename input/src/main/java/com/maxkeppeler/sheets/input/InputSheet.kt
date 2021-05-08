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

@file:Suppress("unused")

package com.maxkeppeler.sheets.input

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.layoutmanagers.CustomGridLayoutManager
import com.maxkeppeler.sheets.input.databinding.SheetsInputBinding
import com.maxkeppeler.sheets.input.type.Input
import com.maxkeppeler.sheets.input.type.InputRadioButtons

/** Listener which returns the inputs with the new data. */
typealias InputListener = (result: Bundle) -> Unit

/**
 * The [InputSheet] lets you display a form consisting of various inputs.
 */
class InputSheet : Sheet() {

    override val dialogTag = "InputSheet"

    companion object {
        const val COLUMNS_MAX_DEFAULT = 1
    }

    private lateinit var binding: SheetsInputBinding
    private lateinit var inputAdapter: InputAdapter
    private var listener: InputListener? = null
    private var inputs = mutableListOf<Input>()
    private var columnsMax: Int = COLUMNS_MAX_DEFAULT

    private val saveAllowed: Boolean
        get() = inputs.filter { it.required }.all { it.valid() }

    /** Set the max amount of columns inputs can span. */
    fun columnsMax(@IntRange(from = 1) columns: Int) {
        this.columnsMax = columns
    }

    /**
     * Set the [InputListener].
     *
     * @param listener Listener that is invoked with the new input data when the positive button is clicked.
     */
    fun onPositive(listener: InputListener) {
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [InputListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the new input data when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, listener: InputListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [InputListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the new input data when the positive button is clicked.
     */
    fun onPositive(positiveText: String, listener: InputListener? = null) {
        this.positiveText = positiveText
        this.listener = listener
    }

    /**
     * Set the text and icon of the positive button and set the [InputListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the new input data when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: InputListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes

        this.listener = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [InputListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the new input data when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: InputListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    /**
     * Add multiple inputs.
     *
     * @param input The [InputRadioButtons] arguments to be added.
     */
    fun with(vararg input: Input) {
        this.inputs.addAll(input.toMutableList())
    }

    /**
     * Add an input.
     *
     * @param input Instance of [InputRadioButtons].
     */
    fun with(input: Input) {
        this.inputs.add(input)
    }

    override fun onCreateLayoutView(): View =
        SheetsInputBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)
        validate(true)
        inputAdapter = InputAdapter(requireContext(), inputs, ::validate)
        with(binding.inputRecyclerView) {
            layoutManager = CustomGridLayoutManager(requireContext(), columnsMax, true).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        inputs[position].columns?.takeUnless { it > columnsMax } ?: columnsMax
                }
            }
            adapter = inputAdapter
            setHasFixedSize(false)
        }
    }

    private fun validate(init: Boolean = false) {
        displayButtonPositive(saveAllowed, !init)
    }

    private fun save() {
        val result = getResult()
        listener?.invoke(result)
        dismiss()
    }

    private fun getResult(): Bundle {
        val bundle = Bundle()
        inputs.forEachIndexed { i, input ->
            input.invokeResultListener()
            input.putValue(bundle, i)
        }
        return bundle
    }

    /** Display input. */
    fun displayInput(key: String, visible: Boolean) {
        if (this::inputAdapter.isInitialized) inputAdapter.displayInput(key, visible)
        else {
            inputs.forEachIndexed { i, comp ->
                if (comp.getKeyOrIndex(i) == key)
                    inputs[i].visible(visible)
            }
        }
    }

    /** Build [InputSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: InputSheet.() -> Unit): InputSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [InputSheet] directly. */
    fun show(ctx: Context, width: Int? = null, func: InputSheet.() -> Unit): InputSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }
}
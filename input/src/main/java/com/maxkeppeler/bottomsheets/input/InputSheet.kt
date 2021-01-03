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

package com.maxkeppeler.bottomsheets.input

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.maxkeppeler.bottomsheets.core.BottomSheet
import com.maxkeppeler.bottomsheets.core.layoutmanagers.CustomLinearLayoutManager
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputBinding
import com.maxkeppeler.bottomsheets.input.type.Input
import com.maxkeppeler.bottomsheets.input.type.InputRadioButtons
import java.io.Serializable

/** Listener which returns the inputs with the new data. */
typealias InputListener = (result: Bundle) -> Unit

/**
 * The [InputSheet] lets you display a form consisting of various inputs.
 */
class InputSheet : BottomSheet() {

    override val dialogTag = "InputSheet"

    companion object {
        private const val STATE_LISTENER = "state_listener"
        private const val STATE_CONTENT_TEXT = "state_content_text"
        private const val STATE_INPUT = "state_input"
        private const val STATE_INPUT_AMOUNT = "state_input_amount"
    }

    private lateinit var binding: BottomSheetsInputBinding

    private var listener: InputListener? = null
    private var contentText: String? = null
    private var input = mutableListOf<Input>()

    private val saveAllowed: Boolean
        get() = input.filter { it.required }.all { it.valid() }

    /**
     * Set the content of the bottom sheet.
     *
     * @param content The text for the content.
     */
    fun content(content: String) {
        this.contentText = content
    }

    /**
     * Set the content of the bottom sheet.
     *
     * @param contentRes The String resource id for the content.
     */
    fun content(@StringRes contentRes: Int) {
        this.contentText = windowContext.getString(contentRes)
    }

    /**
     * Set the content of the bottom sheet.
     *
     * @param contentRes Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *                   substitution.
     */
    fun content(@StringRes contentRes: Int, vararg formatArgs: Any?) {
        this.contentText = windowContext.getString(contentRes, *formatArgs)
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
        this.input.addAll(input.toMutableList())
    }

    /**
     * Add an input.
     *
     * @param input Instance of [InputRadioButtons].
     */
    fun with(input: Input) {
        this.input.add(input)
    }

    override fun onCreateLayoutView(): View =
        BottomSheetsInputBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)

        contentText?.let { binding.content.text = it; binding.content.visibility = View.VISIBLE }

        validate()
        with(binding.inputRecyclerView) {
            layoutManager = CustomLinearLayoutManager(requireContext(), true)
            adapter = InputAdapter(requireContext(), input, ::validate)
        }
    }

    private fun validate() {
        displayButtonPositive(saveAllowed)
    }

    private fun save() {
        listener?.invoke(getResult())
        dismiss()
    }

    private fun getResult(): Bundle {
        val bundle = Bundle()
        input.forEachIndexed { i, input ->
            input.invokeResultListener()
            input.putValue(bundle, i)
        }
        return bundle
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            listener = saved.getSerializable(STATE_LISTENER) as InputListener?
            contentText = saved.getString(STATE_CONTENT_TEXT)
            repeat(saved.getInt(STATE_INPUT_AMOUNT)) {
                val inputItem = saved.getSerializable(STATE_INPUT.plus(it)) as Input
                input.add(inputItem)
            }
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            putSerializable(STATE_LISTENER, listener as Serializable?)
            putString(STATE_CONTENT_TEXT, contentText)
            putInt(STATE_INPUT_AMOUNT, input.size)
            input.forEachIndexed { i, input ->
                putSerializable(STATE_INPUT.plus(i), input)
            }
        }
    }

    /** Build [InputSheet] and show it later. */
    fun build(ctx: Context, func: InputSheet.() -> Unit): InputSheet {
        this.windowContext = ctx
        this.func()
        return this
    }

    /** Build and show [InputSheet] directly. */
    fun show(ctx: Context, func: InputSheet.() -> Unit): InputSheet {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }
}
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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.maxkeppeler.bottomsheets.core.BottomSheet
import com.maxkeppeler.bottomsheets.core.layoutmanagers.CustomLinearLayoutManager
import com.maxkeppeler.bottomsheets.input.databinding.BottomSheetsInputBinding
import com.maxkeppeler.bottomsheets.input.type.Input
import com.maxkeppeler.bottomsheets.input.type.InputRadioButtons

/** Listener which returns the inputs with the new data. */
typealias InputListener = (result: Bundle) -> Unit

@Suppress("unused")
class InputSheet : BottomSheet() {

    override val dialogTag = "InputSheet"

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
        this.positiveText = getString(positiveRes)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View? {
        if (saved != null) dismiss()
        return BottomSheetsInputBinding.inflate(LayoutInflater.from(activity), container, false)
            .also { binding = it }.root
    }

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

    /** Validates, if all required inputs have data. */
    private fun validate() {
        displayButtonPositive(saveAllowed)
    }

    /** Return the result and dismiss dialog. */
    private fun save() {
        listener?.invoke(getResult())
        dismiss()
    }

    /** Puts the values of each input into a bundle to extract data more easily. */
    private fun getResult(): Bundle {
        val bundle = Bundle()
        input.forEachIndexed { i, input ->
            input.invokeResultListener()
            input.putValue(bundle, i)
        }
        return bundle
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
        this.show()
        this.func()
        return this
    }
}
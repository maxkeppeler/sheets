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

package com.maxkeppeler.sheets.input.type.spinner

import android.os.Bundle
import androidx.annotation.StringRes
import com.maxkeppeler.sheets.input.type.Input

/** Listener which returns the new index. */
typealias InputSpinnerListener = (index: Int) -> Unit

/**
 * Input of the type Spinner.
 */
class InputSpinner(key: String? = null, func: InputSpinner.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var changeListener: InputSpinnerListener? = null
    private var resultListener: InputSpinnerListener? = null

    internal var spinnerOptions: List<SpinnerOption>? = null
        private set

    internal var noSelectionText: String? = null
        private set

    internal var textRes: Int? = null
        private set

    var value: Int? = null
        internal set(value) {
            value?.let { changeListener?.invoke(it) }
            field = value
        }

    /** Set the by default selected index. */
    fun selected(selectedIndex: Int) {
        this.value = selectedIndex
    }

    /** Set the options with a list of Strings. */
    @JvmName("optionsStrings")
    fun options(options: List<String>) {
        this.spinnerOptions = options.map { SpinnerOption(it) }
    }

    /** Set the options with a list of StringRes. */
    @JvmName("optionsStringRes")
    fun options(options: List<Int>) {
        this.spinnerOptions = options.map { SpinnerOption(it) }
    }

    /** Set the [SpinnerOption]'s to to be displayed. */
    fun options(options: List<SpinnerOption>) {
        this.spinnerOptions = options
    }

    /** Set the text when no item is selected. */
    fun noSelectionText(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    /** Set the text when no item is selected. */
    fun noSelectionText(text: String) {
        this.noSelectionText = text
    }

    /** Set a listener which returns the new value when it changed. */
    fun changeListener(listener: InputSpinnerListener) {
        this.changeListener = listener
    }

    /** Set a listener which returns the final value when the user clicks the positive button. */
    fun resultListener(listener: InputSpinnerListener) {
        this.resultListener = listener
    }

    override fun invokeResultListener() =
        value?.let { resultListener?.invoke(it) }

    override fun valid(): Boolean = value != -1 && value != null

    override fun putValue(bundle: Bundle, index: Int) {
        value?.let { bundle.putInt(getKeyOrIndex(index), it) }
    }
}
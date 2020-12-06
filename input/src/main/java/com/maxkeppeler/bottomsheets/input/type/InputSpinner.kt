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

package com.maxkeppeler.bottomsheets.input.type

import android.os.Bundle
import androidx.annotation.StringRes

/** Listener which returns the new text and if it is final. */
typealias InputSpinnerListener = (index: Int) -> Unit

@Suppress("unused")
class InputSpinner(key: String? = null, func: InputSpinner.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var changeListener: InputSpinnerListener? = null
    private var resultListener: InputSpinnerListener? = null

    internal var selectedIndex: Int? = null
        private set

    internal var spinnerOptions: MutableList<String>? = null
        private set

    internal var text: String? = null
        private set

    internal var textRes: Int? = null
        private set

    var value: Int = selectedIndex ?: -1
        internal set(value) {
            changeListener?.invoke(value)
            field = value
        }

    fun selected(selectedIndex: Int) {
        this.selectedIndex = selectedIndex
    }

    fun options(options: MutableList<String>) {
        this.spinnerOptions = options
    }

    fun text(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    fun text(text: String) {
        this.text = text
    }

    fun changeListener(listener: InputSpinnerListener) {
        this.changeListener = listener
    }

    fun resultListener(listener: InputSpinnerListener) {
        this.resultListener = listener
    }

    override fun invokeResultListener() =
        resultListener?.invoke(value)

    override fun valid(): Boolean = value != -1

    override fun putValue(bundle: Bundle, index: Int) {
        bundle.putInt(getKeyOrIndex(index), value)
    }
}
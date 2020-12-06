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
typealias CheckBoxInputListener = (value: Boolean) -> Unit

@Suppress("unused")
class InputCheckBox(key: String? = null, func: InputCheckBox.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var changeListener: CheckBoxInputListener? = null
    private var resultListener: CheckBoxInputListener? = null

    internal var text: String? = null
        private set

    internal var textRes: Int? = null
        private set

    internal var default: Boolean = false
        private set

    var value: Boolean = default
        internal set(value) {
            changeListener?.invoke(value)
            field = value
        }

    fun default(default: Boolean) {
        this.default = default
    }

    fun text(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    fun text(text: String) {
        this.text = text
    }

    fun changeListener(listener: CheckBoxInputListener) {
        this.changeListener = listener
    }

    fun resultListener(listener: CheckBoxInputListener) {
        this.resultListener = listener
    }

    override fun invokeResultListener() =
        resultListener?.invoke(value)

    override fun valid(): Boolean = value

    override fun putValue(bundle: Bundle, index: Int) {
        bundle.putBoolean(getKeyOrIndex(index), value)
    }

}
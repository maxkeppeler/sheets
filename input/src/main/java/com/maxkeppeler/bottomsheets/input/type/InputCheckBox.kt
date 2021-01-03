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

package com.maxkeppeler.bottomsheets.input.type

import android.os.Bundle
import androidx.annotation.StringRes

/** Listener which returns the new value. */
typealias CheckBoxInputListener = (value: Boolean) -> Unit

/**
 * Input of the type Checkbox.
 */
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

    var value: Boolean = false
        internal set(value) {
            changeListener?.invoke(value)
            field = value
        }

    /** Set the default value. */
    fun defaultValue(defaultValue: Boolean) {
        this.value = defaultValue
    }

    /** Set the text of the CheckBox. */
    fun text(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    /** Set the text of the CheckBox. */
    fun text(text: String) {
        this.text = text
    }

    /** Set a listener which returns the new value when it changed. */
    fun changeListener(listener: CheckBoxInputListener) {
        this.changeListener = listener
    }

    /** Set a listener which returns the final value when the user clicks the positive button. */
    fun resultListener(listener: CheckBoxInputListener) {
        this.resultListener = listener
    }

    override fun invokeResultListener() =
        resultListener?.invoke(value ?: false)

    override fun valid(): Boolean = value ?: false


    override fun putValue(bundle: Bundle, index: Int) {
        bundle.putBoolean(getKeyOrIndex(index), value ?: false)
    }

}
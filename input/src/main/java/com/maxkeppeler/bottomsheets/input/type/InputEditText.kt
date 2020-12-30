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
import android.text.InputFilter
import android.text.InputType
import androidx.annotation.StringRes

/** Listener which returns the new value. */
typealias EditTextInputListener = (value: String?) -> Unit

/** Listener that is invoked when the value changes and to which a custom validation logic can be executed. */
typealias EditTextInputValidationListener = (value: String /* Non-nullable, text entered can not be null. */) -> ValidationResult
/**
 * Input of the type EditText.
 */
class InputEditText(key: String? = null, func: InputEditText.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var changeListener: EditTextInputListener? = null
    private var resultListener: EditTextInputListener? = null

    internal var hint: String? = null
        private set

    internal var hintRes: Int? = null
        private set

    internal var inputType: Int = InputType.TYPE_CLASS_TEXT
        private set

    internal var inputFilter: InputFilter? = null
        private set

    internal var defaultText: String? = null
        private set

    internal var defaultTextRes: Int? = null
        private set

    var value: String? = null
        internal set(value) {
            changeListener?.invoke(value)
            field = value
        }

    /** Set the default value. */
    fun defaultValue(@StringRes defaultTextRes: Int) {
        this.hintRes = defaultTextRes
    }

    /** Set the default value. */
    fun defaultValue(defaultText: String) {
        this.defaultText = defaultText
    }

    /** Set the hint text. */
    fun hint(@StringRes hintRes: Int) {
        this.hintRes = hintRes
    }

    /** Set the hint text. */
    fun hint(hint: String) {
        this.hint = hint
    }

    /** Set the [InputType]. */
    fun inputType(inputType: Int) {
        this.inputType = inputType
    }

    /** Set the [InputFilter]. */
    fun inputFilter(inputFilter: InputFilter) {
        this.inputFilter = inputFilter
    }

    /** Set a listener which returns the new value when it changed. */
    fun changeListener(listener: EditTextInputListener) {
        this.changeListener = listener
    }

    /** Set a listener which returns the final value when the user clicks the positive button. */
    fun resultListener(listener: EditTextInputListener) {
        this.resultListener = listener
    }

    override fun valid(): Boolean = !defaultText.isNullOrEmpty()
            || !value.isNullOrEmpty()

    override fun invokeResultListener() =
        resultListener?.invoke(value)

    override fun putValue(bundle: Bundle, index: Int) {
        bundle.putString(getKeyOrIndex(index), value)
    }
}
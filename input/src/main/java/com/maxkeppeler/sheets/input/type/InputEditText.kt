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

package com.maxkeppeler.sheets.input.type

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.sheets.input.ValidationResult

/** Listener that returns the new value. */
typealias EditTextInputListener = (value: String?) -> Unit

/** Listener that is invoked when the value changes and to which a custom validation logic can be executed. */
typealias EditTextInputValidationListener = (value: String /* Non-nullable, text entered can not be null. */) -> ValidationResult

/** Listener that is invoked after a custom validation. */
internal typealias EditTextInputValidationResultListener = (validationResult: ValidationResult) -> Unit

/**
 * Input of the type EditText.
 */
class InputEditText(key: String? = null, func: InputEditText.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var validationResultListener: EditTextInputValidationResultListener? = null
    private var validationListener: EditTextInputValidationListener? = null
    private var changeListener: EditTextInputListener? = null
    private var resultListener: EditTextInputListener? = null

    internal var hint: String? = null
        private set

    internal var hintRes: Int? = null
        private set

    internal var inputType: Int? = null
        private set

    internal var inputFilter: InputFilter? = null
        private set

    internal var maxLines: Int? = null
        private set

    internal var endIconMode: Int? = null
        private set

    @DrawableRes
    internal var startIconDrawableRes: Int? = null

    @DrawableRes
    internal var errorIconDrawableRes: Int? = null

    internal var isPasswordVisible: Boolean? = null
        private set

    internal var isEndIconActivated: Boolean? = null
        private set

    internal var suffix: String? = null
        private set

    @StringRes
    internal var suffixRes: Int? = null
        private set

    var value: String? = null
        internal set(value) {
            invokeListeners(value)
            field = value
        }

    private fun invokeListeners(value: String?) {
        changeListener?.invoke(value)
        value?.let { textValue ->
            validationListener?.invoke(textValue)?.let { result ->
                validationResultListener?.invoke(result)
            }
        }
    }

    /** Set the default value. */
    fun defaultValue(@StringRes defaultTextRes: Int) {
        this.hintRes = defaultTextRes
    }

    /** Set the default value. */
    fun defaultValue(defaultText: String) {
        this.value = defaultText
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

    /** Set the max lines. */
    fun maxLines(maxLines: Int) {
        this.maxLines = maxLines
    }

    /** Set a start drawable. */
    fun startIconDrawable(@DrawableRes drawableRes: Int) {
        this.startIconDrawableRes = drawableRes
    }

    /** Set a error drawable. */
    fun errorIconDrawable(@DrawableRes drawableRes: Int) {
        this.errorIconDrawableRes = drawableRes
    }

    /** Set the end icon mode. */
    fun endIconMode(@TextInputLayout.EndIconMode endIconMode: Int) {
        this.endIconMode = endIconMode
    }

    /** Activate the end icon. */
    fun endIconActivated(isEndIconActivated: Boolean) {
        this.isEndIconActivated = isEndIconActivated
    }

    /** Set the initial visibility of the password. This overrides the transformationMethod for the EditText. */
    fun passwordVisible(isPasswordVisible: Boolean) {
        this.isPasswordVisible = isPasswordVisible
    }

    /** Set suffix of edittext. */
    fun suffix(suffix: String) {
        this.suffix = suffix
    }

    /** Set suffix res of edittext. */
    fun suffixRes(suffixRes: Int) {
        this.suffixRes = suffixRes
    }

    /** Set a listener that is invoked when the value needs to be validated. */
    fun validationListener(listener: EditTextInputValidationListener) {
        this.validationListener = listener
    }

    internal fun validationResultListener(listener: EditTextInputValidationResultListener) {
        this.validationResultListener = listener
    }

    /** Set a listener which returns the new value when it changed. */
    fun changeListener(listener: EditTextInputListener) {
        this.changeListener = listener
    }

    /** Set a listener which returns the final value when the user clicks the positive button. */
    fun resultListener(listener: EditTextInputListener) {
        this.resultListener = listener
    }

    override fun valid(): Boolean {
        val customValidationOk = value?.let { validationListener?.invoke(it)?.valid } ?: true
        val requiredValid =
            (required && !value.isNullOrEmpty() || !required)
        return customValidationOk && requiredValid
    }

    override fun invokeResultListener() =
        resultListener?.invoke(value)

    override fun putValue(bundle: Bundle, index: Int) {
        bundle.putString(getKeyOrIndex(index), value)
    }
}
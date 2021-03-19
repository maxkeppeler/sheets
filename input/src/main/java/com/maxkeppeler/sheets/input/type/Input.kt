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
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * The base class of any input. Every input can have a label and a drawable.
 * It can be optional or required.
 */
abstract class Input(private val key: String? = null) {

    internal var required: Boolean = false

    internal var label: String? = null
        private set

    internal var labelRes: Int? = null
        private set

    internal var drawableRes: Int? = null
        private set

    /** Require a value before the user can click the positive button. */
    fun required(required: Boolean = true) {
        this.required = required
    }

    /** Set a drawable. */
    fun drawable(@DrawableRes drawableRes: Int) {
        this.drawableRes = drawableRes
    }

    /** Set the label text. */
    fun label(@StringRes labelRes: Int) {
        this.labelRes = labelRes
    }

    /** Set the label text. */
    fun label(label: String) {
        this.label = label
    }

    /** Check if the input value is valid. */
    abstract fun valid(): Boolean

    /** Invoke the result listener which returns the input value. */
    abstract fun invokeResultListener(): Unit?

    /** Save the input value into the bundle. Takes the index as an key, if there's no unique input key available. */
    abstract fun putValue(bundle: Bundle, index: Int)

    internal fun getKeyOrIndex(index: Int): String =
        if (key.isNullOrEmpty()) index.toString() else key
}
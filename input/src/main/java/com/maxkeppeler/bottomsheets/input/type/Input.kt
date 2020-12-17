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
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

@Suppress("unused")
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

    fun drawable(@DrawableRes drawableRes: Int) {
        this.drawableRes = drawableRes
    }

    fun label(@StringRes labelRes: Int) {
        this.labelRes = labelRes
    }

    fun label(label: String) {
        this.label = label
    }

    /** Check if the input value is valid. */
    internal fun getKeyOrIndex(index: Int): String =
        if (key.isNullOrEmpty()) index.toString() else key

    abstract fun valid(): Boolean

    abstract fun invokeResultListener(): Unit?

    abstract fun putValue(bundle: Bundle, index: Int)
}
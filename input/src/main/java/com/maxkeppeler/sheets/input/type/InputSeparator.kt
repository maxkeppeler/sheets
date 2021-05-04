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

/**
 * Represents a separator view that can be used to create the visual effect that following inputs belong to a specific section.
 */
class InputSeparator(key: String? = null, func: (InputSeparator.() -> Unit)? = null) : Input(key) {

    init {
        func?.invoke(this)
    }

    internal var displayDivider: Boolean? = null
        private set

    override fun invokeResultListener() = Unit

    override fun valid(): Boolean = true

    override fun putValue(bundle: Bundle, index: Int) = Unit

    /** Display divider. */
    fun displayDivider(displayDivider: Boolean) {
        this.displayDivider = displayDivider
    }
}
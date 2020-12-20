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

package com.maxkeppeler.bottomsheets.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * An option is represented with at least a text.
 * A drawable is optional but makes it easier to understand to the user.
 */
class Option internal constructor() {

    @DrawableRes
    internal var drawableRes: Int? = null
        private set

    internal var text: String? = null
        private set

    @StringRes
    internal var textRes: Int? = null
        private set

    internal var selected: Boolean = false
        private set

    internal var disabled: Boolean = false
        private set

    constructor(@StringRes textRes: Int) : this() {
        this.textRes = textRes
    }

    constructor(text: String) : this() {
        this.text = text
    }

    constructor(@DrawableRes drawableRes: Int, text: String) : this() {
        this.drawableRes = drawableRes
        this.text = text
    }

    constructor(@DrawableRes drawableRes: Int, @StringRes textRes: Int) : this() {
        this.drawableRes = drawableRes
        this.textRes = textRes
    }

    /** Declare the option as already selected. */
    fun select(): Option {
        this.selected = true
        return this
    }

    /** Declare the option as already selected. */
    fun selected(selected: Boolean): Option {
        this.selected = selected
        return this
    }

    /** Declare the option as disabled. Disabled options are not selectable anymore. */
    fun disable(): Option {
        this.disabled = true
        return this
    }

    /** Declare the option as disabled. Disabled options are not selectable anymore. */
    fun disabled(disabled: Boolean): Option {
        this.disabled = disabled
        return this
    }
}
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

package com.maxkeppeler.bottomsheets.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class Option(@DrawableRes var drawableRes: Int? = null) {

    var text: String? = null
    @StringRes var textRes: Int? = null

    internal var selected: Boolean = false
            private set

    internal var disabled: Boolean = false
        private set

    constructor(@DrawableRes drawableRes: Int, text: String): this(drawableRes) {
        this.drawableRes = drawableRes
        this.text = text
    }

    constructor(@DrawableRes drawableRes: Int, @StringRes textRes: Int): this(drawableRes) {
        this.drawableRes = drawableRes
        this.textRes = textRes
    }

    fun select(): Option {
        this.selected = true
        return this
    }

    fun selected(selected: Boolean): Option {
        this.selected = selected
        return this
    }

    fun disable(): Option {
        this.disabled = true
        return this
    }

    fun disabled(disabled: Boolean): Option {
        this.disabled = disabled
        return this
    }
}
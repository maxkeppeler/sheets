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

package com.maxkeppeler.sheets.input.type.spinner

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Data class to hold the spinner options
 */
class SpinnerOption internal constructor() {

    internal var drawable: Drawable? = null
        private set

    @DrawableRes
    internal var drawableRes: Int? = null
        private set

    internal var text: String? = null
        private set

    @StringRes
    internal var textRes: Int? = null
        private set

    @ColorRes
    internal var drawableTintRes: Int? = null
        private set

    constructor(text: String) : this() {
        this.drawableRes = drawableRes
        this.text = text
    }

    constructor(textRes: Int) : this() {
        this.drawable = drawable
        this.textRes = textRes
    }

    constructor(@StringRes textRes: Int, drawable: Drawable) : this() {
        this.drawable = drawable
        this.textRes = textRes
    }

    constructor(text: String, drawable: Drawable) : this() {
        this.drawable = drawable
        this.text = text
    }

    constructor(text: String, @DrawableRes drawableRes: Int) : this() {
        this.drawableRes = drawableRes
        this.text = text
    }

    constructor(@StringRes textRes: Int, @DrawableRes drawableRes: Int) : this() {
        this.drawableRes = drawableRes
        this.textRes = textRes
    }

    /** Set tint of drawable. */
    fun drawableTintRes(@ColorRes drawableTintRes: Int): SpinnerOption {
        this.drawableTintRes = drawableTintRes
        return this
    }
}
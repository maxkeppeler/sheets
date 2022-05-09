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

package com.maxkeppeler.sheets.options

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * A info section that is displayed above all options.
 */
class Info internal constructor() {

    @ColorInt
    internal var drawableColor: Int? = null
        private set

    @ColorRes
    internal var drawableColorRes: Int? = null
        private set

    internal var drawable: Drawable? = null
        private set

    @DrawableRes
    internal var drawableRes: Int? = null
        private set

    internal var contentText: CharSequence? = null
        private set

    @StringRes
    internal var contentTextRes: Int? = null
        private set

    constructor(
        @StringRes contentRes: Int? = null,
        drawable: Drawable? = null,
        @ColorInt drawableColor: Int? = null,
    ) : this() {
        this.contentTextRes = contentRes
        this.drawable = drawable
        this.drawableColor = drawableColor
    }

    constructor(
        @StringRes contentRes: Int? = null,
        @DrawableRes drawableRes: Int? = null,
        @ColorInt drawableColor: Int? = null,
    ) : this() {
        this.contentTextRes = contentRes
        this.drawableRes = drawableRes
        this.drawableColor = drawableColor
    }

    constructor(
        @StringRes contentRes: Int? = null,
    ) : this() {
        this.contentTextRes = contentRes
    }

    constructor(
        content: CharSequence? = null,
    ) : this() {
        this.contentText = content
    }

    constructor(
        content: CharSequence? = null,
        @DrawableRes drawableRes: Int? = null,
        @ColorRes drawableColorRes: Int? = null,
    ) : this() {
        this.contentText = content
        this.drawableRes = drawableRes
        this.drawableColorRes = drawableColorRes
    }

    constructor(
        content: CharSequence? = null,
        drawable: Drawable? = null,
        @ColorRes drawableColorRes: Int? = null,
    ) : this() {
        this.contentText = content
        this.drawable = drawable
        this.drawableColorRes = drawableColorRes
    }

    constructor(
        content: CharSequence? = null,
        @StringRes contentRes: Int? = null,
        drawable: Drawable? = null,
        @DrawableRes drawableRes: Int? = null,
        @ColorInt drawableColor: Int? = null,
        @ColorRes drawableColorRes: Int? = null,
    ) : this() {
        this.drawableColor = drawableColor
        this.drawableColorRes = drawableColorRes
        this.drawable = drawable
        this.drawableRes = drawableRes
        this.contentText = content
        this.contentTextRes = contentRes
    }
}
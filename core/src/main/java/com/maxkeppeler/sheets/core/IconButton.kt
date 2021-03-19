/*
 * Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

@file:Suppress("unused")

package com.maxkeppeler.sheets.core

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * Represents an icon acting as a button
 * giving additional possibilities for actions.
 */
class IconButton internal constructor() {

    internal var listener: ClickListener? = null

    @DrawableRes
    internal var drawableRes: Int? = null

    internal var drawable: Drawable? = null

    @ColorRes
    internal var drawableColor: Int? = null

    constructor(@DrawableRes drawableRes: Int) : this() {
        this.drawableRes = drawableRes
    }

    constructor(drawable: Drawable) : this() {
        this.drawable = drawable
    }

    constructor(
        @DrawableRes drawableRes: Int,
        @ColorRes drawableColor: Int
    ) : this() {
        this.drawableRes = drawableRes
        this.drawableColor = drawableColor
    }

    constructor(
        drawable: Drawable,
        @ColorRes drawableColor: Int
    ) : this() {
        this.drawable = drawable
        this.drawableColor = drawableColor
    }

    internal fun listener(listener: ClickListener?) {
        this.listener = listener
    }
}
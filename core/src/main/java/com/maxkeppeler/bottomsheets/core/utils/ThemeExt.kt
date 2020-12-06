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

package com.maxkeppeler.bottomsheets.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat

/** Get a color. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@ColorInt
fun colorOf(ctx: Context, @ColorRes colorRes: Int): Int =
    ContextCompat.getColor(ctx, colorRes)

/** Get a color by a theme attribute. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@ColorInt
fun colorOfAttr(ctx: Context, @AttrRes attr: Int): Int {
    val a = ctx.theme.obtainStyledAttributes(intArrayOf(attr))
    return a.getColor(0, 0)
}

/** Get a color by a theme attribute. */
@SuppressLint("ResourceType")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@ColorInt
fun colorOfAttrs(ctx: Context, @AttrRes vararg attrs: Int): Int {
    val a = ctx.theme.obtainStyledAttributes(attrs.toList().toIntArray())
    attrs.forEachIndexed { index, _ ->
        val color = a.getColor(index, 0)
        if(color != 0) return color
    }
    return 0
}

/** Get a color by a theme attribute of a specific style. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@ColorInt
internal fun colorOfAttrOfTheme(ctx: Context?, @AttrRes resId: Int, @StyleRes stylesId: Int): Int {
    val typedValue = TypedValue()
    val theme = ctx?.resources?.newTheme()
    theme?.applyStyle(stylesId, true)
    theme?.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

/**
 * From [https://github.com/afollestad/material-dialogs/blob/master/core/src/main/java/com/afollestad/materialdialogs/utils/MDUtil.kt]
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Int.isColorDark(threshold: Double = 0.5): Boolean {
    if (this == Color.TRANSPARENT) {
        return false
    }
    val darkness =
        1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return darkness >= threshold
}

/** Get a color by a theme attribute. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun drawableOfAttr(ctx: Context, @AttrRes attr: Int): Drawable? {
    val a = ctx.theme.obtainStyledAttributes(intArrayOf(attr))
    return a.getDrawable(0)
}
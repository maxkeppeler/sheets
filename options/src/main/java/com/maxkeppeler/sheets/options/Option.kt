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
 * Listener that is invoked when an option view is long clicked.
 * This could be used to initiate another action e. g. editing the option in another sheet.
 */
typealias OptionLongClickListener = () -> Unit

/**
 * An option is represented with at least a text.
 * A drawable is optional but makes it easier to understand to the user.
 */
class Option internal constructor() {

    internal var drawable: Drawable? = null
        private set

    @DrawableRes
    internal var drawableRes: Int? = null
        private set

    internal var titleText: String? = null
        private set

    @StringRes
    internal var titleTextRes: Int? = null
        private set

    internal var subtitleText: String? = null
        private set

    @StringRes
    internal var subtitleTextRes: Int? = null
        private set

    @ColorRes
    internal var defaultIconColorRes: Int? = null
        private set

    @ColorInt
    internal var defaultIconColor: Int? = null
        private set

    @ColorRes
    internal var defaultTitleColorRes: Int? = null
        private set

    @ColorInt
    internal var defaultTitleColor: Int? = null
        private set

    @ColorRes
    internal var defaultSubtitleColorRes: Int? = null
        private set

    @ColorInt
    internal var defaultSubtitleColor: Int? = null
        private set

    internal var selected: Boolean = false
        private set

    internal var disabled: Boolean = false
        private set

    internal var longClickListener: OptionLongClickListener? = null
        private set

    internal var preventIconTint: Boolean? = null
        private set

    constructor(@StringRes textRes: Int) : this() {
        this.titleTextRes = textRes
    }

    constructor(text: String) : this() {
        this.titleText = text
    }

    constructor(@DrawableRes drawableRes: Int, titleText: String) : this() {
        this.drawableRes = drawableRes
        this.titleText = titleText
    }

    constructor(drawable: Drawable, @StringRes titleTextRes: Int) : this() {
        this.drawable = drawable
        this.titleTextRes = titleTextRes
    }

    constructor(drawable: Drawable, titleText: String) : this() {
        this.drawable = drawable
        this.titleText = titleText
    }

    constructor(@DrawableRes drawableRes: Int, @StringRes titleTextRes: Int) : this() {
        this.drawableRes = drawableRes
        this.titleTextRes = titleTextRes
    }

    constructor(
        @DrawableRes drawableRes: Int,
        titleText: String,
        subtitleText: String? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawableRes = drawableRes
        this.titleText = titleText
        this.subtitleText = subtitleText
        this.longClickListener = longClickListener
    }

    constructor(
        drawable: Drawable,
        @StringRes titleTextRes: Int,
        subtitleText: String? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawable = drawable
        this.titleTextRes = titleTextRes
        this.subtitleText = subtitleText
        this.longClickListener = longClickListener
    }

    constructor(
        drawable: Drawable,
        titleText: String,
        subtitleText: String? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawable = drawable
        this.titleText = titleText
        this.subtitleText = subtitleText
        this.longClickListener = longClickListener
    }

    constructor(
        @DrawableRes drawableRes: Int,
        @StringRes titleTextRes: Int,
        subtitleText: String? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawableRes = drawableRes
        this.titleTextRes = titleTextRes
        this.subtitleText = subtitleText
        this.longClickListener = longClickListener
    }

    constructor(
        @DrawableRes drawableRes: Int,
        titleText: String,
        @StringRes subtitleTextRes: Int? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawableRes = drawableRes
        this.titleText = titleText
        this.subtitleTextRes = subtitleTextRes
        this.longClickListener = longClickListener
    }

    constructor(
        drawable: Drawable,
        @StringRes titleTextRes: Int,
        @StringRes subtitleTextRes: Int? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawable = drawable
        this.titleTextRes = titleTextRes
        this.subtitleTextRes = subtitleTextRes
        this.longClickListener = longClickListener
    }

    constructor(
        drawable: Drawable,
        titleText: String,
        @StringRes subtitleTextRes: Int? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawable = drawable
        this.titleText = titleText
        this.subtitleTextRes = subtitleTextRes
        this.longClickListener = longClickListener
    }

    constructor(
        @DrawableRes drawableRes: Int,
        @StringRes titleTextRes: Int,
        @StringRes subtitleTextRes: Int? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.drawableRes = drawableRes
        this.titleTextRes = titleTextRes
        this.subtitleTextRes = subtitleTextRes
        this.longClickListener = longClickListener
    }

    constructor(
        @StringRes titleTextRes: Int,
        @StringRes subtitleTextRes: Int? = null,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.titleTextRes = titleTextRes
        this.subtitleTextRes = subtitleTextRes
        this.longClickListener = longClickListener
    }

    constructor(
        titleText: String,
        subtitleText: String,
        longClickListener: OptionLongClickListener? = null
    ) : this() {
        this.titleText = titleText
        this.subtitleText = subtitleText
        this.longClickListener = longClickListener
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

    /**
     * Sheets applies by default a one-colored tint on the drawable representing an option.
     * You can prevent this behavior in order to keep the original colors of the drawable.
     */
    fun preventIconTint(preventIconTint: Boolean): Option {
        this.preventIconTint = preventIconTint
        return this
    }

    /** Set default icon color. */
    fun defaultIconColor(@ColorInt color: Int): Option {
        this.defaultIconColor = color
        return this
    }

    /** Set default icon color. */
    fun defaultIconColorRes(@ColorRes colorRes: Int): Option {
        this.defaultIconColorRes = colorRes
        return this
    }

    /** Set default title color. */
    fun defaultTitleColor(@ColorInt color: Int): Option {
        this.defaultTitleColor = color
        return this
    }

    /** Set default title color. */
    fun defaultTitleColorRes(@ColorRes colorRes: Int): Option {
        this.defaultTitleColorRes = colorRes
        return this
    }

    /** Set default subtitle color. */
    fun defaultSubtitleColor(@ColorInt color: Int): Option {
        this.defaultSubtitleColor = color
        return this
    }

    /** Set default subtitle color. */
    fun defaultSubtitleColorRes(@ColorRes colorRes: Int): Option {
        this.defaultSubtitleColorRes = colorRes
        return this
    }

}
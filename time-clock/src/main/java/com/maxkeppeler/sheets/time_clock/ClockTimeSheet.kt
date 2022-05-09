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

package com.maxkeppeler.sheets.time_clock

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.time_clock.databinding.SheetsTimeClockBinding
import java.util.*

/** Listener which returns the selected clock time in milliseconds. */
typealias ClockTimeListener = (milliseconds: Long, hours: Int, minutes: Int) -> Unit

/**
 * The [ClockTimeSheet] lets you quickly pick a clock time.
 */
class ClockTimeSheet : Sheet() {

    override val dialogTag = "ClockTimeSheet"

    private lateinit var binding: SheetsTimeClockBinding
    private lateinit var selector: ClockTimeSelector

    private var listener: ClockTimeListener? = null
    private var currentTimeInMillis: Long = Calendar.getInstance().timeInMillis
    private var format24Hours: Boolean = true

    /** Set 24-hours format or 12-hours format. Default is 24-hours format. */
    fun format24Hours(format24Hours: Boolean) {
        this.format24Hours = format24Hours
    }

    /** Set current time in milliseconds. */
    fun currentTime(currentTimeInMillis: Long) {
        this.currentTimeInMillis = currentTimeInMillis
    }

    /**
     * Set the [ClockTimeListener].
     *
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(listener: ClockTimeListener) {
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [ClockTimeListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, listener: ClockTimeListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [ClockTimeListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(positiveText: String, listener: ClockTimeListener? = null) {
        this.positiveText = positiveText
        this.listener = listener
    }

    /**
     * Set the text and icon of the positive button and set the [ClockTimeListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: ClockTimeListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawable = ContextCompat.getDrawable(windowContext, drawableRes)
        this.listener = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [ClockTimeListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: ClockTimeListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawable = ContextCompat.getDrawable(windowContext, drawableRes)
        this.listener = listener
    }

    /**
     * Set the text and icon of the positive button and set the [ClockTimeListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawable The drawable for the button icon.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        drawable: Drawable,
        listener: ClockTimeListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawable = drawable
        this.listener = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [ClockTimeListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawable The drawable for the button icon.
     * @param listener Listener that is invoked with the clock time when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        drawable: Drawable,
        listener: ClockTimeListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawable = drawable
        this.listener = listener
    }

    override fun onCreateLayoutView(): View =
        SheetsTimeClockBinding.inflate(LayoutInflater.from(activity))
            .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)
        selector = ClockTimeSelector(
            ctx = requireContext(),
            bindingSelector = binding,
            is24HoursView = format24Hours
        )
        selector.setTime(currentTimeInMillis)
    }

    private fun save() {
        val time = selector.getTime()
        listener?.invoke(time.first, time.second, time.third)
        dismiss()
    }

    /** Build [ClockTimeSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: ClockTimeSheet.() -> Unit): ClockTimeSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [ClockTimeSheet] directly. */
    fun show(ctx: Context, width: Int? = null, func: ClockTimeSheet.() -> Unit): ClockTimeSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }
}
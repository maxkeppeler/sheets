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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.time.databinding.SheetsTimeBinding
import java.io.Serializable

/** Listener which returns the selected duration time in milliseconds. */
typealias DurationTimeListener = (timeInSec: Long) -> Unit

/**
 * The [TimeSheet] lets you pick a duration time in a specific format.
 */
class TimeSheet : Sheet() {

    override val dialogTag = "TimeSheet"

    companion object {
        private const val STATE_LISTENER = "state_listener"
        private const val STATE_FORMAT = "state_format"
        private const val STATE_MIN_TIME = "state_min_time"
        private const val STATE_MAX_TIME = "state_max_time"
        private const val STATE_CURRENT_TIME = "state_current_time"
    }

    private lateinit var binding: SheetsTimeBinding
    private lateinit var selector: TimeSelector

    private var listener: DurationTimeListener? = null
    private var format: TimeFormat = TimeFormat.MM_SS
    private var minTime: Long = Long.MIN_VALUE
    private var maxTime: Long = Long.MAX_VALUE
    private var currentTime: Long? = null
    private var saveAllowed = false

    /** Set the time format. */
    fun format(format: TimeFormat) {
        this.format = format
    }

    /** Set current time in seconds. */
    fun currentTime(currentTimeInSec: Long) {
        this.currentTime = currentTimeInSec
    }

    /** Set min time in seconds. */
    fun minTime(minTimeInSec: Long) {
        this.minTime = minTimeInSec
    }

    /** Set max time in seconds. */
    fun maxTime(maxTimeInSec: Long) {
        this.maxTime = maxTimeInSec
    }

    /**
     * Set the [DurationTimeListener].
     *
     * @param listener Listener that is invoked with the duration time when the positive button is clicked.
     */
    fun onPositive(listener: DurationTimeListener) {
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [DurationTimeListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the duration time when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, listener: DurationTimeListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [DurationTimeListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the duration time when the positive button is clicked.
     */
    fun onPositive(positiveText: String, listener: DurationTimeListener? = null) {
        this.positiveText = positiveText
        this.listener = listener
    }

    /**
     * Set the text and icon of the positive button and set the [DurationTimeListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the duration time when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: DurationTimeListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [DurationTimeListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the duration time when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: DurationTimeListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    private val validationListener: TimeValidationListener = { valid ->
        saveAllowed = valid
        displayButtonPositive(saveAllowed)
    }

    private fun save() {
        listener?.invoke(selector.getTimeInSeconds())
        dismiss()
    }

    override fun onCreateLayoutView(): View =
        SheetsTimeBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)
        selector = TimeSelector(
            requireContext(),
            binding = binding,
            format = format,
            minTime = minTime,
            maxTime = maxTime,
            validationListener = validationListener
        )
        currentTime?.let { selector.setTime(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            listener = saved.getSerializable(STATE_LISTENER) as DurationTimeListener?
            format = saved.getSerializable(STATE_FORMAT) as TimeFormat
            minTime = saved.getLong(STATE_MIN_TIME)
            maxTime = saved.getLong(STATE_MAX_TIME)
            currentTime = saved.get(STATE_CURRENT_TIME) as Long?
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            putSerializable(STATE_LISTENER, listener as Serializable?)
            putSerializable(STATE_FORMAT, format)
            putLong(STATE_MIN_TIME, minTime)
            putLong(STATE_MAX_TIME, maxTime)
            putLong(STATE_CURRENT_TIME, selector.getTimeInSeconds())
        }
    }

    /** Build [TimeSheet] and show it later. */
    fun build(ctx: Context, func: TimeSheet.() -> Unit): TimeSheet {
        this.windowContext = ctx
        this.func()
        return this
    }

    /** Build and show [TimeSheet] directly. */
    fun show(ctx: Context, func: TimeSheet.() -> Unit): TimeSheet {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }
}
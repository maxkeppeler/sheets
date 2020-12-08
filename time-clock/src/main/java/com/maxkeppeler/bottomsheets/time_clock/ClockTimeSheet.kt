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

package com.maxkeppeler.bottomsheets.time_clock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.maxkeppeler.bottomsheets.core.BottomSheet
import com.maxkeppeler.bottomsheets.time_clock.databinding.BottomSheetsTimeClockBinding
import java.util.*

/** Listener which returns the selected clock time in milliseconds. */
typealias ClockTimeListener = (clockTimeInMillis: Long) -> Unit

class ClockTimeSheet : BottomSheet() {

    override val dialogTag = "ClockTimeSheet"

    private lateinit var binding: BottomSheetsTimeClockBinding
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

    override fun onCreateLayoutView(): View =
        BottomSheetsTimeClockBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

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

    /** Return the clock time and dismiss dialog. */
    private fun save() {
        listener?.invoke(selector.getTimeInMillis())
        dismiss()
    }

    /** Build [ClockTimeSheet] and show it later. */
    fun build(ctx: Context, func: ClockTimeSheet.() -> Unit): ClockTimeSheet {
        this.windowContext = ctx
        this.func()
        return this
    }

    /** Build and show [ClockTimeSheet] directly. */
    fun show(ctx: Context, func: ClockTimeSheet.() -> Unit): ClockTimeSheet {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }
}
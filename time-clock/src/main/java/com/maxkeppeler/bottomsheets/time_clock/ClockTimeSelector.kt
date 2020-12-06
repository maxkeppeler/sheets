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

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxkeppeler.bottomsheets.core.utils.colorOfAttrs
import com.maxkeppeler.bottomsheets.core.utils.isAmTime
import com.maxkeppeler.bottomsheets.time_clock.databinding.BottomSheetsTimeClockSelectorBinding
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

internal class ClockTimeSelector(
    private val ctx: Context,
    private val bindingSelector: BottomSheetsTimeClockSelectorBinding,
    private val is24HoursView: Boolean = true
) : View.OnClickListener {

    private val colorTextInactive =
        colorOfAttrs(ctx, R.attr.bottomSheetContentColor, android.R.attr.textColorPrimary)
    private val colorTextActive =
        colorOfAttrs(ctx, R.attr.bottomSheetValueTextActiveColor, R.attr.bottomSheetPrimaryColor, R.attr.colorPrimary)
    private val colorIcons =
        colorOfAttrs(ctx, R.attr.bottomSheetPrimaryColor, R.attr.colorPrimary, R.attr.colorOnSurface)

    private val hoursBuffer = StringBuilder("00")
    private val minsBuffer = StringBuilder("00")

    private var isPositionOnHours = true
    private var currentIndex = 0

    private var isAm = true

    private val keys = mutableListOf<TextView>()

    init {

        with(bindingSelector) {

            hoursInput.setTextColor(colorTextInactive)
            hoursInput.text = getHoursTime()

            minutesInput.setTextColor(colorTextInactive)
            minutesInput.text = getMinutesTime()

            keys.addAll(
                mutableListOf(
                    input.zero,
                    input.one,
                    input.two,
                    input.three,
                    input.four,
                    input.five,
                    input.six,
                    input.seven,
                    input.eight,
                    input.nine
                )
            )
            keys.forEachIndexed { i, v -> v.tag = i; v.setOnClickListener(this@ClockTimeSelector) }

            input.btnRightContainer.setOnClickListener { increaseIndex() }
            input.btnRightIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.bs_ic_arrow_right
                )
            )
            input.btnRightIcon.setColorFilter(colorIcons)

            input.btnLeftContainer.setOnClickListener { reduceIndex() }
            input.btnLeftIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.bs_ic_arrow_left
                )
            )
            input.btnLeftIcon.setColorFilter(colorIcons)

            hoursInput.setOnClickListener { focusOnHours() }
            minutesInput.setOnClickListener { focusOnMinutes() }

            amLabel.visibility = if (is24HoursView) View.GONE else View.VISIBLE
            pmLabel.visibility = if (is24HoursView) View.GONE else View.VISIBLE

            amLabel.setOnClickListener { setAmActive() }
            pmLabel.setOnClickListener { setPmActive() }

            setAmActive()
            proceedIndex()
        }
    }

    /** Inserts digit through numerical input. */
    override fun onClick(v: View?) {

        if (v?.tag == null || v.tag !is Int || !v.isClickable)
            return

        val i = v.tag as Int

        if (isPositionOnHours) {
            if (is24HoursView) {
                if (currentIndex == 0 && i >= 3 && i <= 9) {
                    hoursBuffer[currentIndex] = '0'
                    increaseIndex()
                    hoursBuffer[currentIndex] = i.toString()[0]
                } else hoursBuffer[currentIndex] = i.toString()[0]
            } else {
                if (currentIndex == 0 && i >= 2 && i <= 9) {
                    hoursBuffer[currentIndex] = '0'
                    increaseIndex()
                    hoursBuffer[currentIndex] = i.toString()[0]
                } else hoursBuffer[currentIndex] = i.toString()[0]
            }
            bindingSelector.hoursInput.text = getHoursTime()
        } else {
            minsBuffer[currentIndex] = i.toString()[0]
            bindingSelector.minutesInput.text = getMinutesTime()
        }
        increaseIndex()
    }

    /** Focus on hours view and index, when clicked. */
    private fun focusOnHours() {
        currentIndex = 0
        isPositionOnHours = true
        proceedIndex()
    }

    /** Focus on minutes view and index, when clicked. */
    private fun focusOnMinutes() {
        currentIndex = 0
        isPositionOnHours = false
        proceedIndex()
    }

    /** Jump to current index. */
    private fun proceedIndex() {
        limitKeyboardOnIndexInput()
        setUnderlineToIndex()
    }

    /** Reduce current index. */
    private fun reduceIndex() {
        when {
            currentIndex == 1 -> currentIndex--
            currentIndex < 1 -> {
                isPositionOnHours = !isPositionOnHours
                currentIndex = 1
            }
        }

        setUnderlineToIndex()
        limitKeyboardOnIndexInput()
    }

    /** Increase current index. */
    private fun increaseIndex() {

        when {
            currentIndex == 0 -> currentIndex++
            currentIndex >= 1 -> {
                isPositionOnHours = !isPositionOnHours
                currentIndex = 0
            }
        }

        setUnderlineToIndex()
        limitKeyboardOnIndexInput()
    }

    /** Limit the key of the numerical input depending on index. */
    private fun limitKeyboardOnIndexInput() {

        when {
            isPositionOnHours && currentIndex == 0 -> enableKeys()
            isPositionOnHours && currentIndex == 1 -> {
                enableKeys()
                if (is24HoursView) {
                    if (hoursBuffer[0] == '2')
                        disableKeys(4, 5, 6, 7, 8, 9)
                } else {
                    if (hoursBuffer[0] == '1' || hoursBuffer[0] == '2')
                        disableKeys(3, 4, 5, 6, 7, 8, 9)
                    else if (hoursBuffer[0] == '0') disableKeys(0)
                }
            }

            !isPositionOnHours && currentIndex == 0 -> {
                if (hoursBuffer[0] == '2' && hoursBuffer[1] == '4') {
                    disableKeys()
                } else {
                    enableKeys()
                    disableKeys(6, 7, 8, 9)
                }
            }
            !isPositionOnHours && currentIndex == 1 -> enableKeys()
        }
    }

    /** Display the underline at the current index. */
    private fun setUnderlineToIndex() {

        with(bindingSelector) {

            hoursInput.text = bindingSelector.hoursInput.text.toString()
            minutesInput.text = bindingSelector.minutesInput.text.toString()

            val spanColor = ForegroundColorSpan(colorTextActive)

            if (isPositionOnHours) {

                hoursInput.text = SpannableString(bindingSelector.hoursInput.text).apply {
                    setSpan(
                        spanColor,
                        currentIndex,
                        currentIndex.plus(1),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        UnderlineSpan().apply { },
                        currentIndex,
                        currentIndex.plus(1),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            } else {

                minutesInput.text = SpannableString(bindingSelector.minutesInput.text).apply {
                    setSpan(
                        spanColor,
                        currentIndex,
                        currentIndex.plus(1),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        UnderlineSpan(),
                        currentIndex,
                        currentIndex.plus(1),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
    }

    /** Get current time in milliseconds. */
    fun getTimeInMillis(): Long {

        var hh = getHoursTime().toInt()
        val mm = getMinutesTime().toInt()

        if (!is24HoursView) {
            if (isAm && hh >= 12 && mm > 0) hh -= 12
            else if (!isAm && hh < 12 && mm >= 0) hh += 12
        }

        hh = hh.minus(1)

        if (hh == -1) {
            hh = 23
        }

        val hhMillis = TimeUnit.HOURS.toMillis(hh.toLong())
        val mmMillis = TimeUnit.MINUTES.toMillis(mm.toLong())

        return hhMillis + mmMillis
    }

    /** Set current time. */
    @SuppressLint("SimpleDateFormat")
    fun setTime(timeInMillis: Long) {

        val hours = SimpleDateFormat(if (is24HoursView) "HH" else "hh").format(timeInMillis)
        val minutes = SimpleDateFormat("mm").format(timeInMillis)

        if (isAmTime(timeInMillis)) setAmActive()
        else setPmActive()

        hoursBuffer.replace(0, 2, hours.padStart(2, '0'))
        minsBuffer.replace(0, 2, minutes.padStart(2, '0'))

        bindingSelector.hoursInput.text = getHoursTime()
        bindingSelector.minutesInput.text = getMinutesTime()

        focusOnHours()
    }

    /** Get hours of current input. */
    private fun getHoursTime(): String =
        hoursBuffer.toString().padStart(2, '0')

    /** Get minutes of current input. */
    private fun getMinutesTime(): String =
        minsBuffer.toString().padStart(2, '0')

    /** Set if am is active. */
    private fun setAmActive(active: Boolean = true) {
        bindingSelector.amLabel.setTextColor(if (active) colorTextActive else colorTextInactive)
        bindingSelector.pmLabel.setTextColor(if (active) colorTextInactive else colorTextActive)
        isAm = active
    }

    /** Set if pm is active. */
    private fun setPmActive() {
        setAmActive(false)
    }

    /** Enable all passed indices of keys, or if none passed, all keys. */
    private fun enableKeys(vararg i: Int = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)) {
        i.toMutableList().forEach { keys[it].enableKey() }
    }

    /** Enable a key on the numerical input. */
    private fun TextView.enableKey() {
        isClickable = true
        isFocusable = true
        alpha = 1f
    }

    /** Disable all passed indices of keys, or if none passed, all keys. */
    private fun disableKeys(vararg i: Int = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)) {
        i.toMutableList().forEach { keys[it].disableKey() }
    }

    /** Disable a key on the numerical input. */
    private fun TextView.disableKey() {
        isClickable = false
        isFocusable = false
        alpha = 0.3f
    }
}
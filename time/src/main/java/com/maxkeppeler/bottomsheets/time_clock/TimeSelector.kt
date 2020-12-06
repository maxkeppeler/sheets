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
import android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxkeppeler.bottomsheets.core.utils.colorOfAttrs
import com.maxkeppeler.bottomsheets.core.utils.splitTime
import com.maxkeppeler.bottomsheets.time.R
import com.maxkeppeler.bottomsheets.time.databinding.BottomSheetsTimeSelectorBinding
import java.util.concurrent.TimeUnit

/** Internal listener which informs about the success of the time validation. */
internal typealias TimeValidationListener = (valid: Boolean) -> Unit

internal class TimeSelector(
    private val ctx: Context,
    private val bindingSelector: BottomSheetsTimeSelectorBinding,
    private val format: TimeFormat = TimeFormat.M_SS,
    private val minTime: Long = Long.MIN_VALUE,
    private val maxTime: Long = Long.MAX_VALUE,
    private val validationListener: TimeValidationListener? = null
) : View.OnClickListener {

    private val inactive = colorOfAttrs(
        ctx,
        R.attr.bottomSheetContentColor,
        android.R.attr.textColorPrimary
    )
    private val active =
        colorOfAttrs(ctx, R.attr.bottomSheetPrimaryColor, android.R.attr.colorPrimary)

    private val colorIcons = colorOfAttrs(ctx, R.attr.bottomSheetPrimaryColor, R.attr.colorOnSurface)

    private val time = StringBuffer()
    private val keys = mutableListOf<TextView>()

    init {

        with(bindingSelector) {

            input.btnRightContainer.setOnClickListener { onBackspace() }
            input.btnRightIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.bs_ic_backspace
                )
            )
            input.btnRightIcon.setColorFilter(colorIcons)

            input.btnLeftContainer.setOnClickListener { onClear() }
            input.btnLeftIcon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.bs_ic_clear))
            input.btnLeftIcon.setColorFilter(colorIcons)

            timeValue.text = getFormattedTime()

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

            keys.forEachIndexed { i, v -> v.tag = i; v.setOnClickListener(this@TimeSelector) }
        }

        validationListener?.invoke(false)
    }

    /** Inserts digit through numerical input. */
    override fun onClick(v: View?) {

        if (v?.tag == null || v.tag !is Int || !v.isClickable)
            return

        val i = v.tag as Int

        if (time.length >= format.length) time.deleteCharAt(0)
        time.append(i)
        bindingSelector.timeValue.text = getFormattedTime()
        validate()
    }

    /** Validates if the current time is above the min and below the max time. */
    private fun validate() {

        if (time.isNotEmpty()) {

            val timeInSeconds = getTimeInSeconds()

            when {
                timeInSeconds < minTime -> {
                    bindingSelector.hintLabel.visibility = View.VISIBLE
                    bindingSelector.hintLabel.text =
                        ctx.getString(
                            R.string.at_least_placeholder,
                            getFormattedHintTime(minTime * 1000)
                        )
                    validationListener?.invoke(false)
                }
                timeInSeconds > maxTime -> {
                    bindingSelector.hintLabel.visibility = View.VISIBLE
                    bindingSelector.hintLabel.text = ctx.getString(
                        R.string.at_most_placeholder,
                        getFormattedHintTime(maxTime * 1000)
                    )
                    validationListener?.invoke(false)
                }
                else -> {
                    bindingSelector.hintLabel.visibility = View.GONE
                    validationListener?.invoke(true)
                }
            }
        } else {
            bindingSelector.hintLabel.visibility = View.GONE
            validationListener?.invoke(false)
        }
    }

    /** Calculates the current time in seconds based on the input. */
    fun getTimeInSeconds(): Long {

        var timeInSeconds = 0L
        val reversedTime = time.reversed()
        val timeIntoFormat = StringBuilder(reversedTime)
        for (i in 2..timeIntoFormat.length step 3) {
            timeIntoFormat.insert(i, '_')
        }
        val timeReversedArray = timeIntoFormat.split("_")
        val formatReversedArray = format.name.reversed().split("_")
        formatReversedArray.forEachIndexed { i, formatTimeUnit ->
            if (i >= timeReversedArray.size) return@forEachIndexed
            val time = timeReversedArray[i]
            if (time.isEmpty()) return@forEachIndexed
            when {
                formatTimeUnit.contains("H", ignoreCase = true) -> {
                    timeInSeconds += TimeUnit.HOURS.toSeconds(time.reversed().toLong())
                }
                formatTimeUnit.contains("M", ignoreCase = true) -> {
                    timeInSeconds += TimeUnit.MINUTES.toSeconds(time.reversed().toLong())
                }
                formatTimeUnit.contains("S", ignoreCase = true) -> {
                    timeInSeconds += time.reversed().toInt()
                }
            }
        }

        return timeInSeconds
    }

    /** Removes all digits. */
    private fun onClear() {
        time.setLength(0)
        bindingSelector.timeValue.setText(getFormattedTime(), TextView.BufferType.SPANNABLE)
        validate()
    }

    /** Removes the last digit of the smallest time unit. */
    private fun onBackspace() {

        if (time.isNotEmpty()) time.deleteCharAt(time.lastIndex)

        bindingSelector.timeValue.setText(getFormattedTime(), TextView.BufferType.SPANNABLE)
        validate()
    }

    /** Generate spannable formatted time String. */
    private fun getFormattedTime(): SpannableString {

        val text = SpannableStringBuilder(SpannableString(time))
        repeat(format.length.minus(time.length)) { text.insert(0, "0") }

        text.setSpan(ForegroundColorSpan(inactive), 0, text.lastIndex.plus(1), SPAN_INCLUSIVE_INCLUSIVE)
        val textSizeSmall = ctx.resources.getDimensionPixelSize(R.dimen.textSizeSubheading)

        when (format) {

            TimeFormat.HH_MM_SS -> {
                text.insert(2, ctx.getString(R.string.hour_code))
                text.insert(5, ctx.getString(R.string.minute_code))
                text.insert(8, ctx.getString(R.string.second_code))
                text.insert(3, " ")
                text.insert(7, " ")
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 2, 3, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 6, 7, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 10, 11, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 0, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 4, 6, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 8, 10, SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            TimeFormat.MM_SS -> {
                text.insert(2, ctx.getString(R.string.minute_code))
                text.insert(5, ctx.getString(R.string.second_code))
                text.insert(3, " ")
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 2, 3, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 6, 7, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 0, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 4, 6, SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            TimeFormat.HH_MM -> {
                text.insert(2, ctx.getString(R.string.hour_code))
                text.insert(5, ctx.getString(R.string.minute_code))
                text.insert(3, " ")
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 2, 3, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 6, 7, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 0, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 4, 6, SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            TimeFormat.M_SS -> {
                text.insert(1, ctx.getString(R.string.minute_code))
                text.insert(4, ctx.getString(R.string.second_code))
                text.insert(2, " ")
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 1, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 5, 6, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 0, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 4, 6, SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            TimeFormat.SS -> {
                text.insert(2, ctx.getString(R.string.second_code))
                text.setSpan(AbsoluteSizeSpan(textSizeSmall), 2, 3, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.setSpan(ForegroundColorSpan(active), 0, 2, SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        var index = text.indexOfFirst { it.isDigit() && it != '0' }
        if (index == -1) {
            index = text.lastIndex.minus(1)
        }
        text.setSpan(ForegroundColorSpan(inactive), 0, index, SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(
            UnderlineSpan(),
            text.lastIndex.minus(1),
            text.lastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return SpannableString(text)
    }

    /** Create a formatted spannable time text hint. */
    private fun getFormattedHintTime(timeInMillis: Long): SpannableString {

        val formattedTime = SpannableStringBuilder()

        if (timeInMillis > 0) {

            var millis = timeInMillis
            val days = TimeUnit.MILLISECONDS.toDays(millis).toInt()
            millis -= TimeUnit.DAYS.toMillis(days.toLong())
            val hours = TimeUnit.MILLISECONDS.toHours(millis).toInt()
            millis -= TimeUnit.HOURS.toMillis(hours.toLong())
            var minutes = TimeUnit.MILLISECONDS.toMinutes(millis).toInt()
            millis -= TimeUnit.MINUTES.toMillis(minutes.toLong())
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis).toInt()

            val small16dp =
                ctx.resources.getDimensionPixelSize(R.dimen.textSizeSubheading)
            val big20dp = ctx.resources.getDimensionPixelSize(R.dimen.textSizeTitle)

            if (days > 0) {
                formattedTime.append(days.toString(), AbsoluteSizeSpan(big20dp))
                formattedTime.append(
                    ctx.getString(R.string.day_code),
                    AbsoluteSizeSpan(small16dp)
                )
                formattedTime.append("  ")
            }

            if (hours > 0) {
                formattedTime.append(hours.toString(), AbsoluteSizeSpan(big20dp))
                formattedTime.append(ctx.getString(R.string.hour_code), AbsoluteSizeSpan(small16dp))
                formattedTime.append(" ")
            }

            if (minutes >= 0 || (hours == 0 || days == 0)) {
                if (seconds > 0) minutes = minutes.plus(1)
                formattedTime.append(minutes.toString(), AbsoluteSizeSpan(big20dp))
                formattedTime.append(
                    ctx.getString(R.string.minute_code),
                    AbsoluteSizeSpan(small16dp)
                )
                formattedTime.append(" ")
            }
        }

        return SpannableString(formattedTime)
    }

    private fun SpannableStringBuilder.append(text: CharSequence, span: Any) {
        val textLength = text.length
        append(text)
        setSpan(span, this.length - textLength, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    /** Set current time based on the seconds passed. */
    fun setTime(timeInSeconds: Long) {

        val (days, hours, minutes, seconds) = splitTime(timeInSeconds)

        // No support for days yet
        val filledTimeString = StringBuilder().apply {
            append(hours.toString().padStart(2, '0'))
            append(minutes.toString().padStart(2, '0'))
            append(seconds.toString().padStart(2, '0'))
        }

        filledTimeString.reversed().dropLastWhile { it == '0' }.forEach {
            time.insert(0, it)
        }
        bindingSelector.timeValue.text = getFormattedTime()
        validate()
    }

}
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

package com.maxkeppeler.sheets.core.utils

import androidx.annotation.RestrictTo
import java.text.SimpleDateFormat
import java.util.*

/** Check if the time is am in the 12-hour clock format. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun isAmTime(time: Long): Boolean =
    SimpleDateFormat("a", Locale.US).format(time).equals("am", ignoreCase = true)

/** Splits seconds into days, hours, minutes and seconds. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun splitTime(timeInSeconds: Long): TimeInfo {

    val days = timeInSeconds / 86400
    val hours = timeInSeconds / (60 * 60) % 24
    val minutes = timeInSeconds / 60 % 60
    val sec = timeInSeconds % 60

    return TimeInfo(
        seconds = sec,
        minutes = minutes,
        hours = hours,
        days = days
    )
}

/** Helper class to store time units. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class TimeInfo(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0
) {
    override fun toString(): String {
        return "Time(days=$days, hours=$hours, minutes=$minutes, seconds=$seconds)"
    }
}

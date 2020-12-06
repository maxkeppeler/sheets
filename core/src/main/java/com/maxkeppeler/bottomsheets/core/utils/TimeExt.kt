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
import androidx.annotation.RestrictTo
import java.text.SimpleDateFormat
import java.util.*

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun isAmTime(time: Long): Boolean =
    SimpleDateFormat("a", Locale.US).format(time).equals("am", ignoreCase = true)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class TimeSplit(
    val days: Long = 0,
    val hours: Long = 0,
    val mins: Long = 0,
    val secs: Long = 0
) {

    override fun toString(): String {
        return "Time(days=$days, hours=$hours, mins=$mins, secs=$secs)"
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun splitTime(timeInSeconds: Long): TimeSplit {

    val days = timeInSeconds / 86400
    val hours = timeInSeconds / (60 * 60) % 24
    val mins = timeInSeconds / 60 % 60
    val sec = timeInSeconds % 60

    return TimeSplit(
        secs = sec,
        mins = mins,
        hours = hours,
        days = days
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@SuppressLint("SimpleDateFormat")
fun Long.toFormattedTimeHHMMSS(): String =
    SimpleDateFormat("HH:mm:ss").format(this)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@SuppressLint("SimpleDateFormat")
fun Long.toFormattedTimeHHMM(): String =
    SimpleDateFormat("HH:mm").format(this)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@SuppressLint("SimpleDateFormat")
fun Long.toFormattedTimeMMSS(): String =
    SimpleDateFormat("mm:ss").format(this)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@SuppressLint("SimpleDateFormat")
fun Long.toFormattedDate(): String =
    SimpleDateFormat("d MMM yyyy").format(this)

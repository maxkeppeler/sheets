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

package com.maxkeppeler.sheets.calendar.utils

import androidx.annotation.RestrictTo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/** Converts [Calendar] to [LocalDate]. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Calendar.toLocalDate(): LocalDate =
    LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault()).toLocalDate()

/** Converts [LocalDate] to [Calendar]. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun LocalDate.toCalendar(): Calendar =
    Calendar.getInstance().apply { set(year, month.ordinal, dayOfMonth) }


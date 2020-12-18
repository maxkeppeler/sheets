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

package com.maxkeppeler.sample.utils

import android.annotation.SuppressLint
import androidx.annotation.RestrictTo
import java.text.SimpleDateFormat

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
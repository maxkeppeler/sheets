package com.maxkeppeler.sample.utils

import androidx.annotation.StringRes
import com.maxkeppeler.sample.R

/*
 * Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

enum class SheetType(@StringRes val titleRes: Int) {

    OPTIONS(R.string.options_sheet),

    COLOR(R.string.color_sheet),

    CLOCK_TIME(R.string.clock_time_sheet),

    TIME(R.string.time_sheet),

    CALENDAR(R.string.calendar_sheet),

    INFO(R.string.info_sheet),

    LOTTIE(R.string.lottie),

    INPUT(R.string.input_sheet),

    CUSTOM(R.string.custom_sheets),
}
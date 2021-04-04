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

enum class SheetType(@StringRes val titleRes: Int, @StringRes val descRes: Int) {

    OPTIONS(R.string.options_sheet, R.string.options_sheet_desc),

    COLOR(R.string.color_sheet, R.string.color_sheet_desc),

    CLOCK_TIME(R.string.clock_time_sheet, R.string.clock_time_sheet_desc),

    TIME(R.string.time_sheet, R.string.time_sheet_desc),

    CALENDAR(R.string.calendar_sheet, R.string.calendar_sheet_desc),

    INFO(R.string.info_sheet, R.string.info_sheet_desc),

    LOTTIE(R.string.lottie, R.string.lottie_desc),

    INPUT(R.string.input_sheet, R.string.input_sheet_desc),

    STORAGE(R.string.storage_sheet, R.string.custom_sheets_desc),

    CUSTOM(R.string.custom_sheets, R.string.custom_sheets_desc),
}
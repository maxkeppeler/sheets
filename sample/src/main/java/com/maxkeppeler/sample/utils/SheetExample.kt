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

enum class SheetExample(val type: SheetType, @StringRes val textRes: Int) {

    STORAGE_LIST(SheetType.STORAGE, R.string.storage_list),

    STORAGE_LIST_FILTER(SheetType.STORAGE, R.string.storage_list_filter),

    STORAGE_LIST_COLUMNS(SheetType.STORAGE, R.string.storage_list_columns),

    STORAGE_GRID(SheetType.STORAGE, R.string.storage_grid_columns),

    INFO(SheetType.INFO, R.string.info),

    INFO_COVER_IMAGE_1(SheetType.INFO, R.string.info_cover_image_1),

    INFO_COVER_IMAGE_2(SheetType.INFO, R.string.info_cover_image_2),

    INFO_COVER_IMAGE_3(SheetType.INFO, R.string.info_cover_image_3),

    INFO_LOTTIE(SheetType.LOTTIE, R.string.info_lottie),

    OPTIONS_LIST(SheetType.OPTIONS, R.string.options_list),

    OPTIONS_HORIZONTAL_SMALL(SheetType.OPTIONS, R.string.options_grid_horizontal_small),

    OPTIONS_HORIZONTAL_MIDDLE(SheetType.OPTIONS, R.string.options_grid_horizontal_middle),

    OPTIONS_HORIZONTAL_LARGE(SheetType.OPTIONS, R.string.options_grid_horizontal_large),

    OPTIONS_VERTICAL_SMALL(SheetType.OPTIONS, R.string.options_grid_vertical_small),

    OPTIONS_VERTICAL_MIDDLE(SheetType.OPTIONS, R.string.options_grid_vertical_middle),

    OPTIONS_VERTICAL_LARGE(SheetType.OPTIONS, R.string.options_grid_vertical_large),

    COLOR(SheetType.COLOR, R.string.color_template_custom),

    COLOR_TEMPLATE(SheetType.COLOR, R.string.color_template),

    COLOR_CUSTOM(SheetType.COLOR, R.string.color_custom),

    CLOCK_TIME(SheetType.CLOCK_TIME, R.string.clock_time),

    TIME_HH_MM_SS(SheetType.TIME, R.string.time_hh_mm_ss),

    TIME_HH_MM(SheetType.TIME, R.string.time_hh_mm),

    TIME_MM_SS(SheetType.TIME, R.string.time_mm_ss),

    TIME_M_SS(SheetType.TIME, R.string.time_m_ss),

    TIME_SS(SheetType.TIME, R.string.time_ss),

    TIME_MM(SheetType.TIME, R.string.time_mm),

    TIME_HH(SheetType.TIME, R.string.time_hh),

    CALENDAR_RANGE_MONTH(SheetType.CALENDAR, R.string.calendar_range_month),

    CALENDAR_WEEK1(SheetType.CALENDAR, R.string.calendar_week1),

    CALENDAR_RANGE_WEEK2(SheetType.CALENDAR, R.string.calendar_week2),

    CALENDAR_RANGE_WEEK3(SheetType.CALENDAR, R.string.calendar_week3),

    INPUT_SHORT(SheetType.INPUT, R.string.input_short),

    INPUT_LONG(SheetType.INPUT, R.string.input_long),

    INPUT_PASSWORD(SheetType.INPUT, R.string.input_password),

    CUSTOM1(SheetType.CUSTOM, R.string.custom_sheet_example_1)

}

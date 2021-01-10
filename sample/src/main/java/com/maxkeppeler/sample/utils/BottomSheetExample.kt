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

enum class BottomSheetExample(val type: BottomSheetType, @StringRes val textRes: Int) {

    INFO(BottomSheetType.BOTTOM_SHEET_INFO, R.string.info),

    INFO_COVER_IMAGE_1(BottomSheetType.BOTTOM_SHEET_INFO, R.string.info_cover_image_1),

    INFO_COVER_IMAGE_2(BottomSheetType.BOTTOM_SHEET_INFO, R.string.info_cover_image_2),

    INFO_COVER_IMAGE_3(BottomSheetType.BOTTOM_SHEET_INFO, R.string.info_cover_image_3),

    INFO_LOTTIE(BottomSheetType.BOTTOM_SHEET_LOTTIE, R.string.info_lottie),

    OPTIONS_LIST(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_list),

    OPTIONS_HORIZONTAL_SMALL(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_horizontal_small),

    OPTIONS_HORIZONTAL_MIDDLE(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_horizontal_middle),

    OPTIONS_HORIZONTAL_LARGE(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_horizontal_large),

    OPTIONS_VERTICAL_SMALL(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_vertical_small),

    OPTIONS_VERTICAL_MIDDLE(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_vertical_middle),

    OPTIONS_VERTICAL_LARGE(BottomSheetType.BOTTOM_SHEET_OPTIONS, R.string.options_grid_vertical_large),

    COLOR(BottomSheetType.BOTTOM_SHEET_COLOR, R.string.color_template_custom),

    COLOR_TEMPLATE(BottomSheetType.BOTTOM_SHEET_COLOR, R.string.color_template),

    COLOR_CUSTOM(BottomSheetType.BOTTOM_SHEET_COLOR, R.string.color_custom),

    CLOCK_TIME(BottomSheetType.BOTTOM_SHEET_CLOCK_TIME, R.string.clock_time),

    TIME_HH_MM_SS(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_hh_mm_ss),

    TIME_HH_MM(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_hh_mm),

    TIME_MM_SS(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_mm_ss),

    TIME_M_SS(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_m_ss),

    TIME_SS(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_ss),

    TIME_MM(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_mm),

    TIME_HH(BottomSheetType.BOTTOM_SHEET_TIME, R.string.time_hh),

    CALENDAR_RANGE_MONTH(BottomSheetType.BOTTOM_SHEET_CALENDAR, R.string.calendar_range_month),

    CALENDAR_WEEK1(BottomSheetType.BOTTOM_SHEET_CALENDAR, R.string.calendar_week1),

    CALENDAR_RANGE_WEEK2(BottomSheetType.BOTTOM_SHEET_CALENDAR, R.string.calendar_week2),

    CALENDAR_RANGE_WEEK3(BottomSheetType.BOTTOM_SHEET_CALENDAR, R.string.calendar_week3),

    INPUT_SHORT(BottomSheetType.BOTTOM_SHEET_INPUT, R.string.input_short),

    INPUT_LONG(BottomSheetType.BOTTOM_SHEET_INPUT, R.string.input_long),

    INPUT_PASSWORD(BottomSheetType.BOTTOM_SHEET_INPUT, R.string.input_password),

    CUSTOM1(BottomSheetType.BOTTOM_SHEET_CUSTOM, R.string.custom_sheet_example_1)

}

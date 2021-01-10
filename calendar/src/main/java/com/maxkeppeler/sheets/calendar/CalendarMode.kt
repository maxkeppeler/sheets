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

package com.maxkeppeler.sheets.calendar

/**
 * Calendar modes that can be used.
 */
enum class CalendarMode(internal val rows: Int) {

    /**
     * Week view with 1 row.
     */
    WEEK_1(1),

    /**
     * Week view with 2 rows.
     */
    WEEK_2(2),

    /**
     * Week view with 3 rows.
     */
    WEEK_3(3),

    /**
     * Month view.
     */
    MONTH(6)
}
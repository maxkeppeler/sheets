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
 * There are two selection modes to use.
 */
enum class SelectionMode {

    /**
     * Select one date.
     */
    DATE,

    /**
     * Select a start and end date for receiving a date range.
     */
    RANGE,

    /**
     * Selects multiple dates
     */
    MULTIPLE,
}
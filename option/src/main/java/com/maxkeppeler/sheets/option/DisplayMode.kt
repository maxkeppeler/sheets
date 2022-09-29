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

package com.maxkeppeler.sheets.option

/**
 *  The display modes that can be used.
 *  [LIST], [GRID_HORIZONTAL]
 */
enum class DisplayMode {

    /**
     * Depending on the amount of [Option]s,
     * the options are be displayed in one to two rows with at most 4 options per row.
     * With more than 8 options, the options are displayed in a one row horizontal scrollable view.
     */
    GRID_HORIZONTAL,

    /**
     * Depending on the amount of [Option]s,
     * the options are be displayed in one to two rows with at most 4 options per row.
     * With more than 8 options, the options are displayed in a vertical scrollable view.
     */
    GRID_VERTICAL,


    /**
     * Displays the options in a vertical list.
     */
    LIST
}
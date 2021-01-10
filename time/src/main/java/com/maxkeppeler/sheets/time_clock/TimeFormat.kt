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

package com.maxkeppeler.sheets.time_clock

/**
 * Time formats that can be used.
 */
enum class TimeFormat {

    /** HH:mm:ss (e. g. 12h 10m 30s) */
    HH_MM_SS,

    /** HH:mm (e.g. 20h 30m) */
    HH_MM,

    /** mm:ss (e.g. 12 30m) */
    MM_SS,

    /** m:ss (e.g. 6m 0m) */
    M_SS,

    /** HH (e.g. 8h) */
    HH,

    /** MM (e.g. 12m) */
    MM,

    /** ss (e.g. 45s) */
    SS;

    val length = this.name.filterNot { it == '_' }.length
}
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

package com.maxkeppeler.bottomsheets.core

/**
 * Available top styles.
 */
enum class TopStyle {

    /**
     * All components are above a cover image.
     */
    ICONS_TITLE_TOP, COVER_BOTTOM,


    /**
     * Close and additional icon buttons are within the cover image aligned to the top while the title is out of the image view below
     */
    ICONS_TOP_COVER_MIDDLE_TITLE_BOTTOM,


    /**
     * All components are below a cover image.
     */
    COVER_TOP_ICONS_TITLE_BOTTOM
}
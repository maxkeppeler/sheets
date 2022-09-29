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

package com.maxkeppeler.sheets.core

import androidx.annotation.AttrRes

/**
 * Available button styles.
 */
enum class ButtonStyle(@AttrRes val styleRes: Int) {

    /**
     * Uses the style Widget.MaterialComponents.Button.TextButton.
     */
    TEXT(R.attr.materialButtonOutlinedStyle),

    /**
     * Uses the style Widget.MaterialComponents.Button.OutlinedButton.
     */
    OUTLINED(R.attr.materialButtonOutlinedStyle),

    /**
     * Uses the style Widget.MaterialComponents.Button.
     */
    NORMAL(R.attr.materialButtonStyle),
}
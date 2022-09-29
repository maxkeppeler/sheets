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

package com.maxkeppeler.sheets.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.maxkeppeler.sheets.core.R
import com.maxkeppeler.sheets.core.utils.colorOf
import com.maxkeppeler.sheets.core.utils.colorOfAttr

/** Custom Divider. */
class SheetsDivider
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    styleAttrs: Int = 0,
    styleRes: Int = 0
) : View(ctx, attrs, styleAttrs, styleRes) {

    init {
        val dividerColorDefault = colorOf(ctx, R.color.sheetsDividerColor)
        val dividerColor = colorOfAttr(ctx, R.attr.sheetsDividerColor)
        setBackgroundColor(dividerColor.takeIf { it != 0 } ?: dividerColorDefault)
    }
}

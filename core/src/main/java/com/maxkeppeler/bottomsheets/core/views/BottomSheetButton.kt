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

package com.maxkeppeler.bottomsheets.core.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.utils.toDp

/** Custom text button used for the bottom buttons view. */
class BottomSheetButton
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    styleAttrs: Int = android.R.attr.buttonStyle
) : MaterialButton(ctx, attrs, styleAttrs) {

    init {

        val a = ctx.obtainStyledAttributes(attrs, R.styleable.BottomSheetButton, styleAttrs, 0)

        val fontResId = a.getResourceId(R.styleable.BottomSheetButton_bottomSheetButtonTextFont, 0)
        fontResId.takeIf { it != 0 }?.let { typeface = ResourcesCompat.getFont(ctx, it) }

        val spacing =
            a.getFloat(R.styleable.BottomSheetButton_bottomSheetButtonTextLetterSpacing, 0f)
        spacing.takeIf { it != 0f }?.let { letterSpacing = it }

        a.recycle()
    }
}

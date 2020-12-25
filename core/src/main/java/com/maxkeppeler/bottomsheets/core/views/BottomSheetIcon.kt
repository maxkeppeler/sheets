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
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.bottomsheets.core.utils.getDp
import com.maxkeppeler.bottomsheets.core.utils.getHighlightColor

/** Custom ImageView used for icon buttons on the bottom sheets. */
class BottomSheetIcon
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    styleAttr: Int = 0
) : AppCompatImageView(ctx, attrs, styleAttr) {

    init {

        val shapeModel = ShapeAppearanceModel().toBuilder().apply {
            setAllCorners(CornerFamily.ROUNDED, 45.getDp())
        }.build()

        val shapeDrawable = MaterialShapeDrawable(shapeModel)
        val rippleColor = ColorStateList.valueOf(getHighlightColor(ctx))

        background = RippleDrawable(rippleColor, null, shapeDrawable)
    }
}

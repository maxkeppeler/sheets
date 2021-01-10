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

@file:Suppress("unused")

package com.maxkeppeler.sheets.core.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.sheets.R
import com.maxkeppeler.sheets.core.utils.*

/** Container with a handle drawable. */
internal class SheetHandle
@JvmOverloads constructor(
    val ctx: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(ctx, attrs) {

    companion object {
        private const val DEFAULT_CORNER_FAMILY = CornerFamily.ROUNDED
        private const val DEFAULT_CORNER_RADIUS = 8f
    }

    init {
        orientation = VERTICAL
        setPadding(8.toDp(), 8.toDp(), 8.toDp(), 8.toDp())

        val cornerFamily = intOfAttrs(
            ctx,
            R.attr.sheetHandleCornerFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val cornerRadius = dimensionOfAttrs(
            ctx,
            R.attr.sheetHandleCornerRadius
        ) ?: DEFAULT_CORNER_RADIUS.toDp()

        val fillColor = colorOfAttr(ctx, R.attr.sheetHandleFillColor).takeUnlessNotResolved()
                ?: ContextCompat.getColor(ctx, R.color.sheetDividerColor)

        val borderColor =
            colorOfAttr(ctx, R.attr.sheetHandleBorderColor).takeUnlessNotResolved()
                ?: ContextCompat.getColor(ctx, R.color.sheetDividerColor)

        val borderWidth = dimensionOfAttrs(ctx, R.attr.sheetHandleBorderWidth)

        val shapeModel = ShapeAppearanceModel().toBuilder().apply {
            setAllCorners(cornerFamily, cornerRadius)
        }.build()

        val drawable = MaterialShapeDrawable(shapeModel).apply {
            this.fillColor = ColorStateList.valueOf(fillColor)
            borderWidth?.let { setStroke(it, borderColor) }
        }

        val handleWidth = dimensionOfAttrs(ctx, R.attr.sheetHandleWidth) ?: 28.getDp()
        val handleHeight = dimensionOfAttrs(ctx, R.attr.sheetHandleHeight) ?: 4.getDp()

        addView(ImageView(ctx).apply {
            layoutParams = LayoutParams(
                handleWidth.toInt(),
                handleHeight.toInt()
            ).apply {
                setMargins(0, 8.toDp(), 0, 8.toDp())
            }
            gravity = Gravity.CENTER
            setImageDrawable(drawable)
        })
    }
}

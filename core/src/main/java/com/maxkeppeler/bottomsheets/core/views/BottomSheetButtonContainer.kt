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

package com.maxkeppeler.bottomsheets.core.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.ButtonStyle
import com.maxkeppeler.bottomsheets.core.utils.*

internal typealias ButtonClickListener = () -> Unit

/** Container that contains a button. */
class BottomSheetButtonContainer
@JvmOverloads constructor(
    val ctx: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(ctx, attrs) {

    companion object {
        private const val DEFAULT_CORNER_FAMILY = CornerFamily.ROUNDED
        private const val DEFAULT_CORNER_RADIUS = 8f
        private const val BUTTON_MIN_WIDTH = 120
        private const val BUTTON_ICON_PADDING = 12
    }

    private var negativeBtn: MaterialButton? = null
    private var positiveBtn: MaterialButton? = null

    init {
        orientation = VERTICAL
    }

    private fun createButton(
        btnText: String,
        @DrawableRes btnDrawable: Int?,
        btnListener: ButtonClickListener,
        negative: Boolean,
        shapeModel: ShapeAppearanceModel.Builder
    ) {

        val buttonStyleAttr = if (negative) R.attr.bottomSheetNegativeButtonType
        else R.attr.bottomSheetPositiveButtonType

        val buttonStyleValue = intOfAttrs(ctx, buttonStyleAttr) ?: ButtonStyle.TEXT.ordinal
        val buttonStyle = ButtonStyle.values()[buttonStyleValue]

        val primaryColor = colorOfAttrs(
            ctx,
            R.attr.bottomSheetButtonColor,
            R.attr.bottomSheetPrimaryColor,
            R.attr.colorPrimary
        )
        val rippleColor = getHighlightOfColor(primaryColor)

        val btnWidthLayoutParam =
            intOfAttrs(ctx, R.attr.bottomSheetButtonWidth) ?: ViewGroup.LayoutParams.WRAP_CONTENT

        gravity = Gravity.CENTER

        addView(BottomSheetButton(ctx, null, buttonStyle.styleRes).apply {

            layoutParams =
                ViewGroup.LayoutParams(btnWidthLayoutParam, ViewGroup.LayoutParams.WRAP_CONTENT)

            text = btnText
            btnDrawable?.let { icon = ContextCompat.getDrawable(context, it) }
            iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            iconPadding = BUTTON_ICON_PADDING.toDp()
            iconTint = ColorStateList.valueOf(primaryColor)
            minWidth = BUTTON_MIN_WIDTH.toDp()
            minimumWidth = BUTTON_MIN_WIDTH.toDp()

            setOnClickListener { btnListener.invoke() }

            when (buttonStyle) {
                ButtonStyle.TEXT, ButtonStyle.OUTLINED -> {
                    setRippleColor(ColorStateList.valueOf(rippleColor))
                    setTextColor(primaryColor)
                }
                ButtonStyle.NORMAL -> {
                    setBackgroundColor(primaryColor)
                }
            }

            shapeAppearanceModel = shapeModel.apply {
                if (buttonStyle == ButtonStyle.TEXT) {
                    strokeWidth =
                        0 /* Set border stroke width to zero to remove the border and simulate a normal TextButton. */
                }
            }.build()

        }.also { if (negative) negativeBtn = it else positiveBtn = it })
    }

    /** Setup a negative button. */
    fun setupNegativeButton(
        btnText: String,
        @DrawableRes btnDrawable: Int?,
        btnListener: ButtonClickListener
    ) {

        val parentFamily = R.attr.bottomSheetButtonCornerFamily
        val parentRadius = R.attr.bottomSheetButtonCornerRadius

        val negParentFamily = R.attr.bottomSheetNegativeButtonCornerFamily
        val negParentRadius = R.attr.bottomSheetNegativeButtonCornerRadius

        val negBtnBottomLeftFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonBottomLeftCornerFamily,
            negParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val negBtnBottomRightFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonBottomRightCornerFamily,
            negParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val negBtnTopLeftFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonTopLeftCornerFamily,
            negParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val negBtnTopRightFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonTopRightCornerFamily,
            negParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val negBtnBottomLeftRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonBottomLeftCornerRadius,
            negParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val negBtnBottomRightRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonBottomRightCornerRadius,
            negParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val negBtnTopLeftRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonTopLeftCornerRadius,
            negParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val negBtnTopRightRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetNegativeButtonTopRightCornerRadius,
            negParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val shapeModel = ShapeAppearanceModel().toBuilder().apply {
            setBottomLeftCorner(negBtnBottomLeftFamily, negBtnBottomLeftRadius.toDp())
            setBottomRightCorner(negBtnBottomRightFamily, negBtnBottomRightRadius.toDp())
            setTopLeftCorner(negBtnTopLeftFamily, negBtnTopLeftRadius.toDp())
            setTopRightCorner(negBtnTopRightFamily, negBtnTopRightRadius.toDp())
        }

        createButton(btnText, btnDrawable, btnListener, true, shapeModel)
    }

    /** Setup a positive button. */
    fun setupPositiveButton(
        btnText: String,
        @DrawableRes btnDrawable: Int?,
        btnListener: ButtonClickListener
    ) {

        val parentFamily = R.attr.bottomSheetButtonCornerFamily
        val parentRadius = R.attr.bottomSheetButtonCornerRadius

        val posParentFamily = R.attr.bottomSheetPositiveButtonCornerFamily
        val posParentRadius = R.attr.bottomSheetPositiveButtonCornerRadius

        val posBtnBottomLeftFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonBottomLeftCornerFamily,
            posParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val posBtnBottomRightFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonBottomRightCornerFamily,
            posParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val posBtnTopLeftFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonTopLeftCornerFamily,
            posParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val posBtnTopRightFamily = intOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonTopRightCornerFamily,
            posParentFamily,
            parentFamily
        ) ?: DEFAULT_CORNER_FAMILY

        val posBtnBottomLeftRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonBottomLeftCornerRadius,
            posParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val posBtnBottomRightRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonBottomRightCornerRadius,
            posParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val posBtnTopLeftRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonTopLeftCornerRadius,
            posParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val posBtnTopRightRadius = dimensionOfAttrs(
            ctx,
            R.attr.bottomSheetPositiveButtonTopRightCornerRadius,
            posParentRadius,
            parentRadius
        ) ?: DEFAULT_CORNER_RADIUS

        val shapeModel = ShapeAppearanceModel().toBuilder().apply {
            setBottomLeftCorner(posBtnBottomLeftFamily, posBtnBottomLeftRadius.toDp())
            setBottomRightCorner(posBtnBottomRightFamily, posBtnBottomRightRadius.toDp())
            setTopLeftCorner(posBtnTopLeftFamily, posBtnTopLeftRadius.toDp())
            setTopRightCorner(posBtnTopRightFamily, posBtnTopRightRadius.toDp())
        }

        createButton(btnText, btnDrawable, btnListener, false, shapeModel)
    }

    /** Make positive button clickable. */
    fun positiveButtonClickable(isClickable: Boolean) {
        this.positiveBtn?.isClickable = isClickable
    }

    /** Set positive button listener. */
    fun negativeButtonListener(btnListener: ButtonClickListener) {
        this.negativeBtn?.setOnClickListener { btnListener.invoke() }
    }

    /** Set positive button listener. */
    fun positiveButtonListener(btnListener: ButtonClickListener) {
        this.positiveBtn?.setOnClickListener { btnListener.invoke() }
    }
}

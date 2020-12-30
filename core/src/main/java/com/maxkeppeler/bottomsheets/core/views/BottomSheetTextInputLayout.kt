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
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.utils.getPrimaryColor
import com.maxkeppeler.bottomsheets.core.utils.toDp

/** Custom TextInputLayout. */
class BottomSheetTextInputLayout
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : TextInputLayout(ctx, attrs) {

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 8f
    }

    init {

        val a = ctx.obtainStyledAttributes(attrs, R.styleable.BottomSheetTextInputLayout, 0, 0)

        val cornerRadius = a.getDimension(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutCornerRadius,
            DEFAULT_CORNER_RADIUS.toDp()
        )

        val topLeftCornerRadius = a.getDimension(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutTopLeftCornerRadius,
            cornerRadius
        )

        val topRightCornerRadius = a.getDimension(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutTopRightCornerRadius,
            cornerRadius
        )

        val bottomLeftRadius = a.getDimension(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutBottomLeftCornerRadius,
            cornerRadius
        )

        val bottomRightRadius = a.getDimension(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutBottomRightCornerRadius,
            cornerRadius
        )

        setBoxCornerRadii(
            topLeftCornerRadius,
            topRightCornerRadius,
            bottomLeftRadius,
            bottomRightRadius
        )

        val primaryColor = getPrimaryColor(ctx)

        val endIconColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutEndIconColor,
            0
        )
        endIconColor.takeIf { it != 0 }?.let { setEndIconTintList(ColorStateList.valueOf(it)) }

        val helperTextColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutHelperTextColor,
            primaryColor
        )
        setHelperTextColor(ColorStateList.valueOf(helperTextColor))

        val boxStrokeColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutBoxStrokeColor,
            primaryColor
        )
        setBoxStrokeColorStateList(ColorStateList.valueOf(boxStrokeColor))

        val hintColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutBoxStrokeColor,
            primaryColor
        )
        hintTextColor = ColorStateList.valueOf(hintColor)

        val boxStrokeErrorColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutBoxStrokeErrorColor,
            0
        )
        boxStrokeErrorColor.takeIf { it != 0 }?.let { setBoxStrokeErrorColor(ColorStateList.valueOf(it)) }

        val errorTextColor = a.getColor(
            R.styleable.BottomSheetTextInputLayout_bottomSheetTextInputLayoutErrorTextColor,
            0
        )
        errorTextColor.takeIf { it != 0 }?.let { setErrorTextColor(ColorStateList.valueOf(it)) }
        
        a.recycle()
    }
}

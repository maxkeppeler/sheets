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
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.maxkeppeler.sheets.core.R
import com.maxkeppeler.sheets.core.utils.getPrimaryColor

/** Custom switch button used for the InputSheet. */
class SheetsSwitch
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    styleAttrs: Int = R.attr.switchStyle,
) : SwitchMaterial(ctx, attrs, styleAttrs) {

    val primaryColor = getPrimaryColor(ctx)

    companion object {

        /** Taken from [SwitchMaterial]. */
        private val ENABLED_CHECKED_STATES =
            arrayOf(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_enabled, -android.R.attr.state_checked))
    }

    init {
        thumbTintList = getCustomColorsThumbTintList()
        trackTintList = getCustomColorsTrackTintList()
    }

    /** Taken from [SwitchMaterial]. */
    private fun getCustomColorsThumbTintList(): ColorStateList {
        val elevationOverlayProvider = ElevationOverlayProvider(context)
        val colorSurface = MaterialColors.getColor(this, R.attr.colorSurface)
        var thumbElevation: Float = resources.getDimension(R.dimen.mtrl_switch_thumb_elevation)
        if (elevationOverlayProvider.isThemeElevationOverlayEnabled) {
            thumbElevation += getParentAbsoluteElevation(this)
        }
        val colorThumbOff: Int =
            elevationOverlayProvider.compositeOverlayIfNeeded(colorSurface, thumbElevation)
        val thumbs = IntArray(ENABLED_CHECKED_STATES.size)
        thumbs[0] =
            MaterialColors.layer(colorSurface, primaryColor, MaterialColors.ALPHA_FULL)
        thumbs[1] = colorThumbOff
        thumbs[2] =
            MaterialColors.layer(colorSurface, primaryColor, MaterialColors.ALPHA_DISABLED)
        thumbs[3] = colorThumbOff
        return ColorStateList(ENABLED_CHECKED_STATES, thumbs)
    }

    /** Taken from [SwitchMaterial]. */
    private fun getCustomColorsTrackTintList(): ColorStateList {
        val tracks = IntArray(ENABLED_CHECKED_STATES.size)
        val colorSurface = MaterialColors.getColor(this, R.attr.colorSurface)
        val colorOnSurface = MaterialColors.getColor(this, R.attr.colorOnSurface)
        tracks[0] =
            MaterialColors.layer(colorSurface, primaryColor, MaterialColors.ALPHA_MEDIUM)
        tracks[1] = MaterialColors.layer(colorSurface, colorOnSurface, MaterialColors.ALPHA_LOW)
        tracks[2] = MaterialColors.layer(colorSurface, primaryColor,
            MaterialColors.ALPHA_DISABLED_LOW)
        tracks[3] =
            MaterialColors.layer(colorSurface, colorOnSurface, MaterialColors.ALPHA_DISABLED_LOW)
        return ColorStateList(ENABLED_CHECKED_STATES, tracks)
    }

    /** Taken from [SwitchMaterial]. */
    private fun getParentAbsoluteElevation(view: View): Float {
        var absoluteElevation = 0f
        var viewParent = view.parent
        while (viewParent is View) {
            absoluteElevation += ViewCompat.getElevation((viewParent as View))
            viewParent = viewParent.getParent()
        }
        return absoluteElevation
    }
}

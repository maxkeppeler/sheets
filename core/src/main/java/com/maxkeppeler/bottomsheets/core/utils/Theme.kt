/*
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maxkeppeler.bottomsheets.core.utils

import android.content.Context
import androidx.annotation.StyleRes
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.SheetStyle

internal enum class Theme(@StyleRes val styleRes: Int) {

    BOTTOM_SHEET_DAY(R.style.BottomSheet_Base_Light),
    BOTTOM_SHEET_NIGHT(R.style.BottomSheet_Base_Dark),
    DIALOG_SHEET_DAY(R.style.BottomSheet_Sheet_Base_Light),
    DIALOG_SHEET_NIGHT(R.style.BottomSheet_Sheet_Base_Dark);

    companion object {
        fun inferTheme(ctx: Context, sheetStyle: SheetStyle): Theme {
            val isPrimaryDark = getTextColor(ctx).isColorDark()
            val isBottomSheet = sheetStyle == SheetStyle.BOTTOM_SHEET
            return when {
                isPrimaryDark -> if (isBottomSheet) BOTTOM_SHEET_DAY else DIALOG_SHEET_DAY
                else -> if (isBottomSheet) BOTTOM_SHEET_NIGHT else DIALOG_SHEET_NIGHT
            }
        }
    }
}
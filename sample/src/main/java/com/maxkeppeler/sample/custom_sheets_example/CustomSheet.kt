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

package com.maxkeppeler.sample.custom_sheets_example

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import com.maxkeppeler.sample.databinding.CustomSheetBinding
import com.maxkeppeler.sheets.core.Sheet

private typealias PositiveListener = () -> Unit

@Suppress("unused")
class CustomSheet : Sheet() {

    override val dialogTag = "CustomSheet"

    private lateinit var binding: CustomSheetBinding

    fun onPositive(positiveListener: PositiveListener) {
        this.positiveListener = positiveListener
    }

    fun onPositive(@StringRes positiveRes: Int, positiveListener: PositiveListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveListener = positiveListener
    }

    fun onPositive(positiveText: String, positiveListener: PositiveListener? = null) {
        this.positiveText = positiveText
        this.positiveListener = positiveListener
    }

    /**
     * Implement this method and add your own layout, which will be appended to the default sheet with toolbar and buttons.
     */
    override fun onCreateLayoutView(): View {

        // Inflate layout through binding class and return the root view
        return CustomSheetBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

//        Or without binding
//        return LayoutInflater.from(activity).inflate(R.layout.sheets_custom, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setButtonPositiveListener {  } If you want to override the default positive click listener
//        displayButtonsView() If you want to change the visibility of the buttons view
//        displayButtonPositive() Hiding the positive button will prevent clicks
//        hideToolbar() Hide the toolbar of the sheet, the title and the icon
    }

    /** Build [CustomSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: CustomSheet.() -> Unit): CustomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [CustomSheet] directly. */
    fun show(ctx: Context, width: Int? = null, func: CustomSheet.() -> Unit): CustomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }
}
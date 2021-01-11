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
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.maxkeppeler.sample.databinding.BottomSheetsCustomBinding
import com.maxkeppeler.sheets.core.PositiveListener
import com.maxkeppeler.sheets.core.Sheet

@Suppress("unused")
class CustomSheet : Sheet() {

    override val dialogTag = "CustomSheet"
    private var contentText: String? = null

    private lateinit var binding: BottomSheetsCustomBinding

    companion object {
        private const val STATE_CONTENT_TEXT = "state_content_text"
    }


    /**
     * Set the content of the sheet.
     *
     * @param content The text for the content.
     */
    fun content(content: String) {
        this.contentText = content
    }

    /**
     * Set the content of the sheet.
     *
     * @param contentRes The String resource id for the content.
     */
    fun content(@StringRes contentRes: Int) {
        this.contentText = windowContext.getString(contentRes)
    }

    /**
     * Set the content of the sheet.
     *
     * @param contentRes Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *                   substitution.
     */
    fun content(@StringRes contentRes: Int, vararg formatArgs: Any?) {
        this.contentText = windowContext.getString(contentRes, *formatArgs)
    }

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
     * Set the text and icon of the positive button and optionally a listener.
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(
            positiveText: String,
            @DrawableRes drawableRes: Int,
            positiveListener: PositiveListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.positiveListener = positiveListener
    }

    /**
     * Set the text and icon of the positive button and optionally a listener.
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(
            @StringRes positiveRes: Int,
            @DrawableRes drawableRes: Int,
            positiveListener: PositiveListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.positiveListener = positiveListener
    }

    /**
     * Implement this method and add your own layout, which will be appended to the default sheet with toolbar and buttons.
     */
    override fun onCreateLayoutView(): View {

        // Inflate layout through binding class and return the root view
        return BottomSheetsCustomBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root
//        Or without binding
//        return LayoutInflater.from(activity).inflate(R.layout.sheets_custom, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setButtonPositiveListener {  } If you want to override the default positive click listener
//        displayButtonsView() If you want to change the visibility of the buttons view
//        displayButtonPositive() Hiding the positive button will prevent clicks
//        hideToolbar() Hide the toolbar of the sheet, the title and the icon
        with(binding) {
            contentText?.let { content.text = it }
        }
    }

    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            // Restore data after configuration change and before view is build
            contentText = saved.getString(STATE_CONTENT_TEXT)
            val active = saved.getBoolean("active")
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            // Save data before configuration change
            putBoolean("active", true)
            putString(STATE_CONTENT_TEXT, contentText)
        }
    }


    /** Build [CustomSheet] and show it later. */
    fun build(ctx: Context, func: CustomSheet.() -> Unit): CustomSheet {
        this.windowContext = ctx
        this.func()
        return this
    }

    /** Build and show [CustomSheet] directly. */
    fun show(ctx: Context, func: CustomSheet.() -> Unit): CustomSheet {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }
}
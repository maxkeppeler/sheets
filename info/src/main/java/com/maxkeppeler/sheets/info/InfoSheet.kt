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

package com.maxkeppeler.sheets.info

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.maxkeppeler.sheets.core.PositiveListener
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.utils.getIconColor
import com.maxkeppeler.sheets.info.databinding.SheetsInfoBinding

/** Listener that is invoked when the view is ready. */
typealias CustomViewListener = (view: View) -> Unit

/**
 * The [InfoSheet] lets you display an information or warning.
 */
class InfoSheet : Sheet() {

    override val dialogTag = "InfoSheet"

    private lateinit var binding: SheetsInfoBinding

    private var contentText: String? = null
    private var displayButtons = true
    private var customViewRes: Int? = null
    private var customView: View? = null
    private var customViewListener: CustomViewListener? = null
    private var drawable: Drawable? = null

    @DrawableRes
    private var drawableRes: Int? = null

    @ColorInt
    private var drawableColor: Int? = null

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

    /** Set a drawable left of the text. */
    fun drawable(@DrawableRes drawableRes: Int) {
        this.drawableRes = drawableRes
        this.drawable = null
    }

    /** Set a drawable left of the text. */
    fun drawable(drawable: Drawable) {
        this.drawable = drawable
        this.drawableRes = null
    }

    /** Set the color of the drawable. */
    fun drawableColor(@ColorRes colorRes: Int) {
        this.drawableColor = ContextCompat.getColor(windowContext, colorRes)
    }

    /** Set a custom view. */
    fun customView(@LayoutRes layout: Int, listener: CustomViewListener? = null) {
        this.customViewRes = layout
        this.customViewListener = listener
        this.customView = null
    }

    /** Set a custom view. */
    fun customView(view: View, listener: CustomViewListener? = null) {
        this.customView = view
        this.customViewListener = listener
        this.customViewRes = null
    }

    /**
     * Set a listener.
     *
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveListener: PositiveListener) {
        this.positiveListener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveText The text for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveText: String, positiveListener: PositiveListener? = null) {
        this.positiveText = positiveText
        this.positiveListener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveRes The String resource id for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, positiveListener: PositiveListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
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
        positiveListener: PositiveListener? = null,
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
        positiveListener: PositiveListener? = null,
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.positiveListener = positiveListener
    }

    /** Display buttons and require a positive button click. */
    fun displayButtons(displayButtons: Boolean = true) {
        this.displayButtons = displayButtons
    }

    override fun onCreateLayoutView(): View =
        SheetsInfoBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayButtonsView(displayButtons)
        with(binding) {

            val customView = customView ?: customViewRes?.let { res ->
                LayoutInflater.from(requireContext()).inflate(res, null, false)
            }

            customView?.let {
                binding.root.removeAllViews()
                binding.root.addView(customView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT))
            } ?: run {
                contentText?.let { content.text = it }
                if (drawableRes != null || drawable != null) {
                    val infoIconDrawable = drawable ?: drawableRes?.let {
                        ContextCompat.getDrawable(
                            requireContext(),
                            it)
                    }
                    icon.setImageDrawable(infoIconDrawable)
                    icon.setColorFilter(drawableColor ?: getIconColor(requireContext()))
                    icon.visibility = View.VISIBLE
                }
            }
        }
    }

    /** Build [InfoSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: InfoSheet.() -> Unit): InfoSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [InfoSheet] directly. */
    fun show(ctx: Context, width: Int? = null, func: InfoSheet.() -> Unit): InfoSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }
}
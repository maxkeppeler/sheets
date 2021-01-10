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

package com.maxkeppeler.bottomsheets.color

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.maxkeppeler.bottomsheets.color.databinding.BottomSheetsColorBinding
import com.maxkeppeler.bottomsheets.core.Sheet
import com.maxkeppeler.bottomsheets.core.layoutmanagers.CustomGridLayoutManager
import com.maxkeppeler.bottomsheets.core.utils.*
import com.maxkeppeler.bottomsheets.core.views.BottomSheetContent
import java.io.Serializable


/** Listener to be invoked when color is selected. */
typealias ColorListener = (Int) -> Unit

/**
 * The [ColorSheet] lets you pick a color.
 */
class ColorSheet : Sheet(), SeekBar.OnSeekBarChangeListener {

    override val dialogTag = "ColorSheet"

    private companion object {
        private const val ARG_MAX_VALUE = 255
        private const val STATE_LISTENER = "state_listener"
        private const val STATE_DISABLE_ALPHA = "state_disable_alpha"
        private const val STATE_DEFAULT_COLOR = "state_default_color"
        private const val STATE_SELECTED_COLOR = "state_selected_color"
        private const val STATE_SWITCH_COLOR_VIEW = "state_switch_color"
        private const val STATE_COLORS = "state_colors"
        private const val STATE_COLOR_VIEW = "state_color_view"
    }

    private lateinit var binding: BottomSheetsColorBinding

    private val argbSeekBars = mutableListOf<SeekBar>()
    private val argbLabelTexts = mutableListOf<String>()
    private val argbLabels = mutableListOf<BottomSheetContent>()
    private val argbValues = mutableListOf<BottomSheetContent>()

    private var colorAdapter: ColorAdapter? = null

    @ColorRes
    private var colorMapListRes: MutableList<Int> = getDefaultColorPalette().toMutableList()

    private var colorView = ColorView.TEMPLATE
    private var switchColorView = true

    private val touchCustomColorView
        get() = switchColorView || colorView == ColorView.CUSTOM

    private var defaultColor: Int? = null
    private var selectedColor: Int = Color.BLACK

    private var iconColor = 0
    private var primaryColor = 0
    private var highlightColor = 0

    private var listener: ColorListener? = null
    private var disableAlpha: Boolean = false

    private val saveAllowed: Boolean
        get() = selectedColor != defaultColor

    /** Set default [ColorView]. */
    fun defaultView(colorView: ColorView) {
        this.colorView = colorView
    }

    /** Disable to switch between [ColorView]. */
    fun disableSwitchColorView() {
        this.switchColorView = false
    }

    /** Set default color. */
    fun defaultColor(@ColorRes color: Int) {
        this.defaultColor = color
    }

    /** Set colors. */
    fun colors(@ColorRes vararg color: Int) {
        this.colorMapListRes = color.toMutableList()
    }

    /** Set colors. */
    fun colors(@ColorRes colors: MutableList<Int>) {
        this.colorMapListRes = colors
    }

    /** Disable alpha. */
    fun disableAlpha() {
        this.disableAlpha = true
    }

    /**
     * Set the [ColorListener].
     *
     * @param listener Listener that is invoked with the selected color when the positive button is clicked.
     */
    fun onPositive(listener: ColorListener) {
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [ColorListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the selected color when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, listener: ColorListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [ColorListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the selected color when the positive button is clicked.
     */
    fun onPositive(positiveText: String, listener: ColorListener? = null) {
        this.positiveText = positiveText
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [ColorListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected color when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: ColorListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [ColorListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected color when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: ColorListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    override fun onCreateLayoutView(): View =
        BottomSheetsColorBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)
        displayToolbarTypeButton(switchColorView)

        iconColor = getIconColor(requireContext())
        primaryColor = getPrimaryColor(requireContext())
        highlightColor = getHighlightColor(requireContext())

        when {
            switchColorView -> {
                setupCustomView()
                setupTemplatesView()
            }
            colorView == ColorView.CUSTOM -> {
                setupCustomView()
            }
            colorView == ColorView.TEMPLATE -> {
                setupTemplatesView()
            }
        }

        setColorView()

        if (switchColorView) {
            setToolbarTypeButtonListener {
                colorView = ColorView.TEMPLATE.takeUnless { it == colorView } ?: ColorView.CUSTOM
                setColorView()
            }
        }
    }

    private fun validate() {
        displayButtonPositive(saveAllowed)
    }

    private fun save() {
        listener?.invoke(selectedColor)
        dismiss()
    }

    private fun setupTemplatesView() {
        if (colorMapListRes.isNotEmpty()) {
            binding.colorTemplatesView.layoutManager =
                CustomGridLayoutManager(requireContext(), 6, true)
            colorAdapter = ColorAdapter(requireContext(), colorMapListRes, selectedColor) { color ->
                selectedColor = color
                updateColor()
                validate()
            }
            binding.colorTemplatesView.adapter = colorAdapter
        }
    }

    private fun setupCustomView() {

        with(binding.custom) {

            argbSeekBars.apply {
                add(alphaSeekBar)
                add(redSeekBar)
                add(greenSeekBar)
                add(blueSeekBar)
            }

            argbLabels.apply {
                add(alphaLabel)
                add(redLabel)
                add(greenLabel)
                add(blueLabel)
            }

            argbValues.apply {
                add(alphaValue)
                add(redValue)
                add(greenValue)
                add(blueValue)
            }

            argbLabelTexts.apply {
                add(getString(R.string.color_picker_alpha))
                add(getString(R.string.color_picker_red))
                add(getString(R.string.color_picker_green))
                add(getString(R.string.color_picker_blue))
            }

            argbSeekBars.forEach { seekBar ->
                seekBar.max = ARG_MAX_VALUE
                seekBar.progressBackgroundTintList = ColorStateList.valueOf(iconColor)
                seekBar.progressTintList = ColorStateList.valueOf(primaryColor)
                seekBar.thumbTintList = ColorStateList.valueOf(primaryColor)
                seekBar.setOnSeekBarChangeListener(this@ColorSheet)
            }

            btnCopy.setColorFilter(iconColor)
            btnPaste.setColorFilter(iconColor)

            if (disableAlpha) {
                argbLabels.first().visibility = View.GONE
                argbSeekBars.first().visibility = View.GONE
                argbValues.first().visibility = View.GONE
            }

            val maxLength: Int = argbLabelTexts.minByOrNull { it.length }!!.length
            argbLabels.forEach { it.widthByLength(maxLength) }
            argbLabelTexts.forEachIndexed { i, s -> argbLabels[i].text = s }

            hexValue.text = getHex(defaultColor ?: selectedColor)
            updateColor()

            btnCopy.setOnClickListener { onCopy() }
            btnPaste.setOnClickListener { onPaste() }
        }
    }

    private fun onCopy() {
        val hex = getHex(selectedColor)
        copyIntoClipboard(requireContext(), "color", hex)
    }

    private fun onPaste() {
        val pastedText = pasteFromClipboard(requireContext())
        pastedText?.let {
            try {
                selectedColor = Color.parseColor(it)
                binding.custom.hexValue.text = it
                updateColor()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.clipboard_paste_invalid_color_code),
                    Toast.LENGTH_LONG
                ).show()
            }
        } ?: kotlin.run {
            // Nothing to paste from clipboard.
            Toast.makeText(
                requireContext(),
                getString(R.string.clipboard_paste_invalid_empty),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun setColorView() {

        val templateView = colorView == ColorView.TEMPLATE

        with(binding) {
            colorTemplatesView.visibility = if (templateView) View.VISIBLE else View.GONE
            custom.root.visibility = if (templateView) View.INVISIBLE else View.VISIBLE
            if (switchColorView) {
                setToolbarTypeButtonDrawable(if (templateView) R.drawable.bs_ic_color_picker else R.drawable.bs_ic_color_palette)
            }
        }
    }

    private fun getHex(color: Int): String =
        String.format("#%08X", (0xFFFFFFFF and color.toLong()))

    private fun updateColor(calculate: Boolean = false) {

        with(binding.custom) {

            val colorParsed = Color.parseColor(getHex(selectedColor))
            val a = if (calculate) argbSeekBars[0].progress else Color.alpha(colorParsed)
            val r = if (calculate) argbSeekBars[1].progress else Color.red(colorParsed)
            val g = if (calculate) argbSeekBars[2].progress else Color.green(colorParsed)
            val b = if (calculate) argbSeekBars[3].progress else Color.blue(colorParsed)

            if (calculate) {
                selectedColor = Color.argb(a, r, g, b)
            } else {
                if (touchCustomColorView) {
                    argbSeekBars[0].progress = a
                    argbSeekBars[1].progress = r
                    argbSeekBars[2].progress = g
                    argbSeekBars[3].progress = b
                }
            }

            if (touchCustomColorView) {
                val background = customColorView.background as RippleDrawable
                (background.getDrawable(1) as GradientDrawable).setColor(selectedColor)

                alphaValue.text = a.toString().padStart(3)
                redValue.text = r.toString().padStart(3)
                greenValue.text = g.toString().padStart(3)
                blueValue.text = b.toString().padStart(3)

                hexValue.text = getHex(selectedColor)
            }
        }

        validate()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        updateColor(true)
        if (fromUser) {
            colorAdapter?.removeSelection()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            listener = saved.getSerializable(STATE_LISTENER) as ColorListener?
            colorView = saved.getSerializable(STATE_COLOR_VIEW) as ColorView
            saved.getIntArray(STATE_COLORS)?.let { colorMapListRes = it.toMutableList() }
            disableAlpha = saved.getBoolean(STATE_DISABLE_ALPHA)
            switchColorView = saved.getBoolean(STATE_SWITCH_COLOR_VIEW)
            defaultColor = saved.getInt(STATE_DEFAULT_COLOR)
            selectedColor = saved.getInt(STATE_SELECTED_COLOR)
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            putSerializable(STATE_LISTENER, listener as Serializable?)
            putSerializable(STATE_COLOR_VIEW, colorView as Serializable)
            putIntArray(STATE_COLORS, colorMapListRes.toIntArray())
            putBoolean(STATE_DISABLE_ALPHA, disableAlpha)
            putBoolean(STATE_SWITCH_COLOR_VIEW, switchColorView)
            defaultColor?.let { putInt(STATE_DEFAULT_COLOR, it) }
            putInt(STATE_SELECTED_COLOR, selectedColor)
        }
    }

    /** Build [ColorSheet] and show it later. */
    fun build(ctx: Context, func: ColorSheet.() -> Unit): ColorSheet {
        this.windowContext = ctx
        this.func()
        return this
    }

    /** Build and show [ColorSheet] directly. */
    fun show(ctx: Context, func: ColorSheet.() -> Unit): ColorSheet {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }
}


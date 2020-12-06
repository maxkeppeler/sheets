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

package com.maxkeppeler.bottomsheets.color

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.maxkeppeler.bottomsheets.color.databinding.BottomSheetsColorPickerBinding
import com.maxkeppeler.bottomsheets.core.BottomSheet
import com.maxkeppeler.bottomsheets.core.layoutmanagers.CustomGridLayoutManager
import com.maxkeppeler.bottomsheets.core.utils.colorOfAttrs
import com.maxkeppeler.bottomsheets.core.utils.copyIntoClipboard
import com.maxkeppeler.bottomsheets.core.utils.pasteFromClipboard
import com.maxkeppeler.bottomsheets.core.utils.widthByLength
import com.maxkeppeler.bottomsheets.core.views.BottomSheetContent


/** Listener to be invoked when color is selected. */
typealias ColorListener = (Int) -> Unit

@Suppress("unused")
class ColorSheet : BottomSheet(), SeekBar.OnSeekBarChangeListener {

    override val dialogTag = "ColorSheet"

    private lateinit var binding: BottomSheetsColorPickerBinding

    private companion object {
        private const val ARG_MAX_VALUE = 255
    }

    private val argbSeekBars = mutableListOf<SeekBar>()
    private val argbLabelTexts = mutableListOf<String>()
    private val argbLabels = mutableListOf<BottomSheetContent>()
    private val argbValues = mutableListOf<BottomSheetContent>()

    private var colorAdapter: ColorAdapter? = null

    private var colorMapListRes: MutableList<Int> = COLORS_PALETTE.toMutableList()
    private var colorView = ColorView.TEMPLATE

    private var switchColorView = true

    private var defaultColor: Int = Color.BLACK
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
    fun defaultColor(color: Int) {
        this.defaultColor = color
    }

    /** Set colors. */
    fun colors(@ColorRes vararg color: Int): ColorSheet {
        this.colorMapListRes.addAll(color.toMutableList())
        return this
    }

    /** Disable alpha. */
    fun disableAlpha(): ColorSheet {
        this.disableAlpha = true
        return this
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) dismiss()
        return BottomSheetsColorPickerBinding.inflate(
            LayoutInflater.from(activity),
            container,
            false
        ).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)

        iconColor = colorOfAttrs(
            requireContext(),
            R.attr.bottomSheetIconsColor, R.attr.colorOnSurface
        )

        primaryColor = colorOfAttrs(
            requireContext(),
            R.attr.bottomSheetPrimaryColor,
            android.R.attr.colorPrimary
        )

        highlightColor = colorOfAttrs(
            requireContext(),
            R.attr.bottomSheetHighlightColor,
            android.R.attr.colorControlHighlight
        )

        when {
            switchColorView -> {
                setupCustomView()
                setupTemplatesView()
            }
            colorView == ColorView.CUSTOM -> setupCustomView()
            colorView == ColorView.TEMPLATE -> setupTemplatesView()
        }

        setColorView()
    }

    /** Validate selection. */
    private fun validate() {
        displayButtonPositive(saveAllowed)
    }

    /** Return the current selection and dismiss dialog. */
    private fun save() {
        listener?.invoke(selectedColor)
        dismiss()
    }

    /** Setup teamplates colors view. */
    private fun setupTemplatesView() {
        if (colorMapListRes.isNotEmpty()) {
            binding.colorTemplatesView.layoutManager =
                CustomGridLayoutManager(requireContext(), 6, true)
            colorAdapter = ColorAdapter(requireContext(), colorMapListRes) { color ->
                selectedColor = color
                updateColor()
                validate()
            }
            binding.colorTemplatesView.adapter = colorAdapter
        }
    }

    /** Setup SeekBar for alpha, red, green and blue values. */
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

            val maxLength: Int = argbLabelTexts.minBy { it.length }!!.length
            argbLabels.forEach { it.widthByLength(maxLength) }
            argbLabelTexts.forEachIndexed { i, s -> argbLabels[i].text = s }

            hexValue.text = getHex(defaultColor)
            updateColor()

            btnCopy.setOnClickListener { onCopy() }
            btnPaste.setOnClickListener { onPaste() }

            binding.top.btnColorSource.setOnClickListener {
                colorView = ColorView.TEMPLATE.takeUnless { it == colorView } ?: ColorView.CUSTOM
                setColorView()
            }
        }
    }

    /** Copy current color into clipboard. */
    private fun onCopy() {
        val hex = getHex(selectedColor)
        copyIntoClipboard(requireContext(), "color", hex)
    }

    /** Paste color from clipboard. */
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


    /** Set color view. */
    private fun setColorView() {

        val templateView = colorView == ColorView.TEMPLATE

        with(binding) {
            colorTemplatesView.visibility = if (templateView) View.VISIBLE else View.GONE
            custom.root.visibility = if (templateView) View.INVISIBLE else View.VISIBLE

            if (switchColorView) {
                top.btnColorSource.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        if (templateView) R.drawable.bs_ic_color_picker else R.drawable.bs_ic_color_palette
                    )
                )
                top.btnColorSource.visibility = View.VISIBLE
            } else {
                top.btnColorSource.visibility = View.GONE
            }
        }

    }

    /** Get hex string from color. */
    private fun getHex(color: Int): String =
        String.format("#%08X", (0xFFFFFFFF and color.toLong()))

    /** Update the view with current selected color or calculate based on values of SeekBars. **/
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
                argbSeekBars[0].progress = a
                argbSeekBars[1].progress = r
                argbSeekBars[2].progress = g
                argbSeekBars[3].progress = b
            }

            val background = customColorView.background as RippleDrawable
            (background.getDrawable(1) as GradientDrawable).setColor(selectedColor)

            alphaValue.text = a.toString().padStart(3)
            redValue.text = r.toString().padStart(3)
            greenValue.text = g.toString().padStart(3)
            blueValue.text = b.toString().padStart(3)

            hexValue.text = getHex(selectedColor)
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


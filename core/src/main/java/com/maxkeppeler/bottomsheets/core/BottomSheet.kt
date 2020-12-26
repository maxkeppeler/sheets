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

package com.maxkeppeler.bottomsheets.core

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.utils.*
import com.maxkeppeler.bottomsheets.core.utils.Theme
import com.maxkeppeler.bottomsheets.databinding.BottomSheetsBaseBinding

/** Listener which is invoked when the positive button is clicked. */
typealias PositiveListener = () -> Unit

/** Listener which is invoked when the negative button is clicked. */
typealias NegativeListener = () -> Unit

/** Listener which is invoked when the bottom sheet is dismissed. */
typealias DismissListener = () -> Unit

/** Listener which is invoked when buttons are clicked. */
typealias ClickListener = () -> Unit

/**
 * This class is the base of all types of bottom sheets.
 * You can implement this class in your own and build your
 * own custom bottom sheet with the already existing features which the base class offers.
 */
abstract class BottomSheet : BottomSheetDialogFragment() {

    open val dialogTag = "BottomSheet"

    companion object {
        const val DEFAULT_CORNER_RADIUS = 16f
        const val DEFAULT_CORNER_FAMILY = CornerFamily.ROUNDED
        const val ICON_BUTTONS_AMOUNT_MAX = 3
    }

    open lateinit var windowContext: Context

    private var theme = Theme.DAY

    lateinit var bindingBase: BottomSheetsBaseBinding

    private var hideToolbar: Boolean = false
    private var hideCloseButton: Boolean = false
    private var titleText: String? = null
    private var btnCloseDrawable: Drawable? = null

    protected var positiveText: String? = null
    private var negativeText: String? = null

    protected var positiveButtonDrawable: Drawable? = null
    private var negativeButtonDrawable: Drawable? = null

    private var dismissListener: DismissListener? = null
    protected var positiveListener: PositiveListener? = null
    private var negativeListener: NegativeListener? = null

    private var state = BottomSheetBehavior.STATE_EXPANDED
    private var peekHeight = 0
    private var cornerRadiusDp: Float? = null
    private var borderStrokeWidthDp: Float? = null
    private var borderStrokeColor: Int? = null
    private var cornerFamily: Int? = null

    private var iconButtons: Array<IconButton?> = arrayOfNulls(ICON_BUTTONS_AMOUNT_MAX)

    /**
     * Add an icon button to the bottom sheet toolbar to the right of the title.
     * The icon buttons will be aligned from the right in the order you call this method.
     * You can only add up to 3 icon buttons.
     * @throws IllegalStateException If you add more than 3 icon buttons.
     */
    fun withIconButton(iconButton: IconButton, listener: ClickListener? = null) {
        val index = this.iconButtons.indexOfFirst { it == null }
        if (index == -1) throw IllegalStateException("You can only add 3 icon buttons.")
        this.iconButtons[index] = iconButton.apply { listener(listener) }
    }

    /** Set if bottom sheet is cancelable outside. */
    fun cancelableOutside(cancelable: Boolean) {
        this.isCancelable = cancelable
    }

    /** Set the [BottomSheetBehavior] state. */
    fun state(state: Int) {
        this.state = state
    }

    /** Set the peek height. */
    fun peekHeight(peekHeight: Int) {
        this.peekHeight = peekHeight
    }

    /** Set the [CornerFamily]. */
    fun cornerFamily(cornerFamily: Int) {
        this.cornerFamily = cornerFamily
    }

    /** Set the corner radius in dp. */
    fun cornerRadius(cornerRadiusInDp: Float) {
        this.cornerRadiusDp = cornerRadiusInDp
    }

    /** Set the corner radius in dp. */
    fun cornerRadius(@DimenRes cornerRadiusInDpRes: Int) {
        this.cornerRadiusDp = windowContext.resources.getDimension(cornerRadiusInDpRes)
    }

    /** Set the border stroke width */
    fun borderWidth(strokeWidthDp: Float) {
        this.borderStrokeWidthDp = strokeWidthDp
    }

    /** Set the border stroke width */
    fun borderWidth(@DimenRes strokeWidthDpRes: Int) {
        this.borderStrokeWidthDp = windowContext.resources.getDimension(strokeWidthDpRes)
    }

    /**
     * Set the border stroke color around the bottom sheet.
     * If no color is set, the primary color is used.
     */
    fun borderColor(strokeColor: Int) {
        this.borderStrokeColor = strokeColor
    }

    /**
     * Hide the toolbar.
     *
     * The toolbar consists of the close icon button, the title and the divider.
     * For some bottom sheet's it may hide more controls.
     */
    fun hideToolbar() {
        this.hideToolbar = true
    }

    /** Set the Close Button Drawable. */
    fun closeButtonDrawable(closeDrawable: Drawable) {
        this.btnCloseDrawable = closeDrawable
    }

    /** Set the Close Button Drawable. */
    fun closeButtonDrawable(@DrawableRes drawableRes: Int) {
        this.btnCloseDrawable = ContextCompat.getDrawable(windowContext, drawableRes)
    }

    /** Hide the close icon button */
    fun hideCloseButton() {
        this.hideCloseButton = true
    }

    /**
     * Set the title of the bottom sheet.
     *
     * @param title The text for the title.
     */
    fun title(title: String) {
        this.titleText = title
    }

    /**
     * Set the title of the bottom sheet.
     *
     * @param titleRes The String resource id for the title.
     */
    fun title(@StringRes titleRes: Int) {
        this.titleText = windowContext.getString(titleRes)
    }

    /**
     * Set the title of the bottom sheet.
     *
     * @param titleRes Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *                   substitution.
     */
    fun title(@StringRes titleRes: Int, vararg formatArgs: Any?) {
        this.titleText = windowContext.getString(titleRes, *formatArgs)
    }

    /**
     * Set a listener.
     *
     * @param negativeListener Listener that is invoked when the negative button is clicked.
     */
    fun onNegative(negativeListener: NegativeListener) {
        this.negativeListener = negativeListener
    }

    /**
     * Set the text of the negative button and optionally a listener.
     *
     * @param negativeRes The String resource id for the negative button.
     * @param negativeListener Listener that is invoked when the negative button is clicked.
     */
    fun onNegative(@StringRes negativeRes: Int, negativeListener: NegativeListener? = null) {
        this.negativeText = windowContext.getString(negativeRes)
        this.negativeListener = negativeListener
    }

    /**
     * Set the text of the negative button and optionally a listener.
     *
     * @param negativeText The text for the negative button.
     * @param negativeListener Listener that is invoked when the negative button is clicked.
     */
    fun onNegative(negativeText: String, negativeListener: NegativeListener? = null) {
        this.negativeText = negativeText
        this.negativeListener = negativeListener
    }

    /**
     * Set the text and drawable of the negative button and optionally a listener.
     *
     * @param negativeRes The String resource id for the negative button.
     * @param negativeListener Listener that is invoked when the negative button is clicked.
     * @param drawableRes The drawable resource for the button icon.
     */
    fun onNegative(
        @StringRes negativeRes: Int,
        @DrawableRes drawableRes: Int,
        negativeListener: NegativeListener? = null
    ) {
        this.negativeText = windowContext.getString(negativeRes)
        this.negativeButtonDrawable = ContextCompat.getDrawable(windowContext, drawableRes)
        this.negativeListener = negativeListener
    }

    /**
     * Set the text and drawable of the negative button and optionally a listener.
     *
     * @param negativeText The text for the negative button.
     * @param negativeListener Listener that is invoked when the negative button is clicked.
     * @param drawableRes The drawable resource for the button icon.
     */
    fun onNegative(
        negativeText: String,
        @DrawableRes drawableRes: Int,
        negativeListener: NegativeListener? = null
    ) {
        this.negativeText = negativeText
        this.negativeButtonDrawable = ContextCompat.getDrawable(windowContext, drawableRes)
        this.negativeListener = negativeListener
    }

    /** Sets a listener that is invoked when the bottom sheet is dismissed. */
    fun onDismiss(dismissListener: DismissListener? = null) {
        this.dismissListener = dismissListener
    }

    /** Override theme to allow auto switch between day & night design. */
    override fun getTheme(): Int {
        theme = Theme.inferTheme(requireContext())
        return theme.styleRes
    }

    /** Override dismiss to trigger custom dismiss listener. */
    override fun dismiss() {
        super.dismiss()
        dismissListener?.invoke()
    }

    /** Create view of base bottom sheet. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View? {
        if (saved != null) dismiss()
        return BottomSheetsBaseBinding.inflate(LayoutInflater.from(activity), container, false)
            .also { bindingBase = it }.apply {
                layout.addView(onCreateLayoutView())
            }.root
    }

    /** Create custom view to be added to the base bottom sheet. */
    abstract fun onCreateLayoutView(): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetBehavior(view)
        setupBottomSheetBackground(view)
        setupBottomSheet()
    }

    private fun setupBottomSheetBehavior(view: View) {

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog? ?: return
                val behavior = dialog.behavior
                behavior.state = state
                behavior.peekHeight = peekHeight
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, dY: Float) {
                        // TODO: Make button layout stick to the bottom through translationY property.
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        })
    }

    private fun setupBottomSheetBackground(view: View) {

        val cornerFamily =
            cornerFamily ?: getCornerFamily(requireContext()) ?: DEFAULT_CORNER_FAMILY
        val cornerRadius =
            cornerRadiusDp ?: getCornerRadius(requireContext()) ?: DEFAULT_CORNER_RADIUS

        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(cornerFamily, cornerRadius.toDp())
            setTopLeftCorner(cornerFamily, cornerRadius.toDp())
        }.build()

        val shape = MaterialShapeDrawable(model).apply {

            borderStrokeWidthDp?.let { width ->
                setStroke(width.toDp(), borderStrokeColor ?: getPrimaryColor(requireContext()))
                setPadding(width.getDp(), width.getDp(), width.getDp(), width.getDp())
            }

            val backgroundColor = getBottomSheetBackgroundColor(requireContext(), theme.styleRes)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }

    private fun setupBottomSheet() {

        val iconsColor = getIconColor(requireContext())

        if (hideToolbar) {
            bindingBase.top.root.visibility = View.GONE
        } else {
            bindingBase.top.root.visibility = View.VISIBLE
            val isCloseButtonVisible = !hideCloseButton && isDisplayCloseButton(requireContext())
            if (isCloseButtonVisible) {
                btnCloseDrawable?.let { bindingBase.top.btnClose.setImageDrawable(it) }
                bindingBase.top.btnClose.setColorFilter(iconsColor)
                bindingBase.top.btnClose.setOnClickListener { dismiss() }
                bindingBase.top.btnClose.visibility = View.VISIBLE
            } else {
                bindingBase.top.btnClose.visibility = View.GONE
            }
            titleText?.let { bindingBase.top.title.text = it }
        }

        bindingBase.top.btnType.setColorFilter(iconsColor)

        bindingBase.top.btnExtra1.setColorFilter(iconsColor)
        bindingBase.top.btnExtra2.setColorFilter(iconsColor)
        bindingBase.top.btnExtra3.setColorFilter(iconsColor)

        setupIconButtons()
        setupButtonsView()
    }

    private fun setupIconButtons() {

        iconButtons.filterNotNull().forEachIndexed { i, btn ->
            btn.drawable?.let { drawable -> setToolbarExtraButtonDrawable(i, drawable) }
            btn.drawableRes?.let { drawableRes -> setToolbarExtraButtonDrawable(i, drawableRes) }
            btn.listener?.let { listener -> setToolbarExtraButtonListener(i, listener) }
            displayToolbarExtraButton(i)
        }
    }


    private fun setToolbarExtraButtonDrawable(i: Int, @DrawableRes drawableRes: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableRes)
        setToolbarExtraButtonDrawable(i, drawable)
    }

    private fun setToolbarExtraButtonDrawable(i: Int, drawable: Drawable?) {
        with(bindingBase.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setImageDrawable(drawable)
        }
    }

    private fun displayToolbarExtraButton(i: Int) {
        with(bindingBase.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.visibility = View.VISIBLE
        }
    }

    private fun setupButtonsView() {

        bindingBase.buttons.btnNegativeContainer.setupNegativeButton(
            btnText = negativeText ?: getString(R.string.cancel),
            btnDrawable = negativeButtonDrawable
        ) { negativeListener?.invoke(); dismiss() }

        bindingBase.buttons.btnPositiveContainer.setupPositiveButton(
            btnText = positiveText ?: getString(R.string.ok),
            btnDrawable = positiveButtonDrawable
        ) { positiveListener?.invoke(); dismiss() }
    }

    /** Display the positive button. */
    protected fun displayButtonPositive(display: Boolean) {
        bindingBase.buttons.btnPositiveContainer.apply {
            isClickable = display
            if (display) fadeIn() else fadeOut()
        }
    }

    /** Show positive button */
    protected fun showButtonPositive() {
        bindingBase.buttons.btnPositiveContainer.fadeIn()
        bindingBase.buttons.btnPositiveContainer.isClickable = true
    }

    /** Hide positive button. */
    protected fun hideButtonPositive() {
        bindingBase.buttons.btnPositiveContainer.fadeOut()
        bindingBase.buttons.btnPositiveContainer.isClickable = false
    }

    /** Display buttons view. */
    protected fun displayButtonsView(display: Boolean) {
        bindingBase.buttons.root.visibility = if (display) View.VISIBLE else View.GONE
    }

    /** Set a listener which is invoked when the positive button is clicked. */
    protected fun setButtonPositiveListener(listener: ClickListener) {
        bindingBase.buttons.btnPositiveContainer.positiveButtonListener(listener)
    }

    /** Set a listener which is invoked when the type icon button is clicked. */
    protected fun setToolbarTypeButtonListener(listener: ClickListener) {
        bindingBase.top.btnType.setOnClickListener { listener.invoke() }
    }

    /** Set a drawable for the type icon button. */
    protected fun setToolbarTypeButtonDrawable(@DrawableRes drawableRes: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableRes)
        bindingBase.top.btnType.setImageDrawable(drawable)
        bindingBase.top.btnType.visibility = View.VISIBLE
    }

    /** Display the type icon button. */
    protected fun displayToolbarTypeButton(display: Boolean) {
        bindingBase.top.btnType.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun setToolbarExtraButtonListener(i: Int, listener: ClickListener) {
        with(bindingBase.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setOnClickListener { listener.invoke() }
        }
    }

    /** Show the bottom sheet. */
    fun show() {
        windowContext.let { ctx ->
            when (ctx) {
                is FragmentActivity -> show(ctx.supportFragmentManager, dialogTag)
                is AppCompatActivity -> show(ctx.supportFragmentManager, dialogTag)
                is Fragment -> show(ctx.childFragmentManager, dialogTag)
                is PreferenceFragmentCompat -> show(ctx.childFragmentManager, dialogTag)
                else -> throw IllegalStateException("Context has no window attached.")
            }
        }
    }
}
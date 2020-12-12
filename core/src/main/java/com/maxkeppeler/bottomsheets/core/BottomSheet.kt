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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.bottomsheets.core.utils.*
import com.maxkeppeler.bottomsheets.core.utils.Theme
import com.maxkeppeler.bottomsheets.databinding.BottomSheetsBaseBinding

/** Listener which is invoked when the positive button is clicked. */
typealias PositiveListener = () -> Unit

/** Listener which is invoked when the negative button is clicked. */
typealias NegativeListener = () -> Unit

/** Listener which is invoked when the bottom sheet is dismissed. */
typealias DismissListener = () -> Unit

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class BottomSheet : BottomSheetDialogFragment() {

    open val dialogTag = "BottomSheet"

    companion object {
        const val DEFAULT_CORNER_RADIUS = 16f
        const val DEFAULT_CORNER_FAMILY = CornerFamily.ROUNDED
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

    private var dismissListener: DismissListener? = null
    protected var positiveListener: PositiveListener? = null
    private var negativeListener: NegativeListener? = null

    private var state = BottomSheetBehavior.STATE_EXPANDED
    private var peekHeight = 0
    private var cornerRadiusDp: Float? = null
    private var borderStrokeWidthDp: Float? = null
    private var borderStrokeColor: Int? = null
    private var cornerFamily: Int? = null

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

    /** Setup the bottom sheet behavior. */
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

    /** Setup the bottom sheet background appearance. */
    private fun setupBottomSheetBackground(view: View) {

        val cornerFamily = cornerFamily?: getCornerFamily(requireContext()) ?: DEFAULT_CORNER_FAMILY
        val cornerRadius = cornerRadiusDp?: getCornerRadius(requireContext()) ?: DEFAULT_CORNER_RADIUS

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

    /** Setup the bottom sheet appearance. */
    private fun setupBottomSheet() {

        if (hideToolbar) {
            bindingBase.top.root.visibility = View.GONE
        } else {
            bindingBase.top.root.visibility = View.VISIBLE

            if (hideCloseButton) bindingBase.top.btnClose.visibility = View.VISIBLE
            else bindingBase.top.btnClose.setOnClickListener { dismiss() }
            titleText?.let { bindingBase.top.title.text = it }
        }

        val colorCloseIcon = getIconColor(requireContext())

        btnCloseDrawable?.let { bindingBase.top.btnClose.setImageDrawable(it) }
        bindingBase.top.btnClose.setColorFilter(colorCloseIcon)
        bindingBase.top.btnExtra.setColorFilter(colorCloseIcon)

        positiveText?.let { bindingBase.buttons.btnPositive.text = it }
        bindingBase.buttons.btnPositive.setOnClickListener { positiveListener?.invoke(); dismiss() }

        negativeText?.let { bindingBase.buttons.btnNegative.text = it }
        bindingBase.buttons.btnNegative.setOnClickListener { negativeListener?.invoke(); dismiss() }
    }

    /** Display positive button. */
    fun displayButtonPositive(display: Boolean) {
        if (display) showButtonPositive()
        else hideButtonPositive()
    }

    /** Show positive button */
    private fun showButtonPositive() {
        bindingBase.buttons.btnPositive.fadeIn()
        bindingBase.buttons.btnPositive.isClickable = true
    }

    /** Hide positive button. */
    fun hideButtonPositive() {
        bindingBase.buttons.btnPositive.fadeOut()
        bindingBase.buttons.btnPositive.isClickable = false
    }

    /** Display buttons view. */
    fun displayButtonsView(display: Boolean) {
        if (display) showButtonsView()
        else hideButtonsView()
    }

    /** Show buttons view. */
    fun showButtonsView() {
        bindingBase.buttons.root.visibility = View.VISIBLE
    }

    /** Hide buttons view. */
    fun hideButtonsView() {
        bindingBase.buttons.root.visibility = View.GONE
    }

    /** Set a listener which is invoked when the positive button is clicked. */
    fun setButtonPositiveListener(clickListener: () -> Unit) {
        bindingBase.buttons.btnPositive.setOnClickListener { clickListener.invoke() }
    }

    /** Set a listener which is invoked when the positive button is clicked. */
    fun setToolbarExtraButtonListener(clickListener: () -> Unit) {
        bindingBase.top.btnExtra.setOnClickListener { clickListener.invoke() }
    }

    /** Set a listener which is invoked when the positive button is clicked. */
    fun setToolbarExtraButtonDrawable(@DrawableRes drawableRes: Int) {
        bindingBase.top.btnExtra.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), drawableRes)
        )
    }

    /** Display extra button in toolbar. */
    fun displayToolbarExtraButton(display: Boolean) {
        if (display) showToolbarExtraButton()
        else hideToolbarExtraButton()
    }

    /** Show extra button in toolbar. */
    fun showToolbarExtraButton() {
        bindingBase.top.btnExtra.visibility = View.VISIBLE
    }

    /** Hide extra button in toolbar. */
    fun hideToolbarExtraButton() {
        bindingBase.top.btnExtra.visibility = View.GONE
    }

    /** Show the bottom sheet. */
    fun show() {
        windowContext.let { ctx ->
            when (ctx) {
                is FragmentActivity -> show(ctx.supportFragmentManager, dialogTag)
                is AppCompatActivity -> show(ctx.supportFragmentManager, dialogTag)
                is Fragment -> show(ctx.childFragmentManager, dialogTag)
                else -> throw IllegalStateException("Context has no window attached.")
            }
        }
    }
}
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

package com.maxkeppeler.sheets.core

import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.loadAny
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.sheets.R
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.databinding.SheetsBaseBinding

/** Listener that is invoked when the positive button is clicked. */
typealias PositiveListener = () -> Unit

/** Listener that is invoked when the negative button is clicked. */
typealias NegativeListener = () -> Unit

/** Listener that is invoked when the sheet is dismissed through a positive or negative action. */
typealias DismissListener = () -> Unit

/** Listener that is invoked when the sheet is cancelled. Works only if sheet is cancelable. */
typealias CancelListener = () -> Unit

/** Listener that is invoked when the sheet is closed. */
typealias CloseListener = () -> Unit

/** Listener that is invoked when buttons are clicked. */
typealias ClickListener = () -> Unit

/**
 * Bundle a block of sheet functions for the use case of add-on-components.
 * AddOnComponents maintain their state.
 */
typealias AddOnComponent = Sheet.() -> Unit

/** Listener that is invoked when the view was created. */
typealias OnViewCreatedListener = (SheetsBaseBinding) -> Unit

/**
 * This class is the base of all types of sheets.
 * You can implement this class in your own and build your
 * own custom sheet with the already existing features which the base class offers.
 */
abstract class Sheet : SheetFragment() {

    override val dialogTag = "Sheet"

    companion object {
        const val DEFAULT_DISPLAY_HANDLE = false
        const val DEFAULT_DISPLAY_CLOSE_BUTTON = true
        const val ICON_BUTTONS_AMOUNT_MAX = 3
    }

    private lateinit var base: SheetsBaseBinding

    private var addOnComponents = mutableListOf<AddOnComponent>()
    private var onCreateViewListeners = mutableListOf<OnViewCreatedListener>()
    protected var positiveListener: PositiveListener? = null
    private var negativeListener: NegativeListener? = null
    private var dismissListener: DismissListener? = null
    private var cancelListener: CancelListener? = null
    private var closeListener: CloseListener? = null

    private var topStyle = TopStyle.ABOVE_COVER
    private var displayNegativeButton: Boolean = true
    private var displayPositiveButton: Boolean = true
    private var negativeButtonStyle: ButtonStyle? = null
    private var positiveButtonStyle: ButtonStyle? = null

    @ColorInt
    private var negativeButtonColor: Int? = null

    @ColorInt
    private var positiveButtonColor: Int? = null

    private var useCover: Boolean = false
    private var coverImage: Image? = null
    private var coverAnimationView: Any? = null

    private var displayToolbar: Boolean? = null
    private var displayCloseButton: Boolean? = null
    private var displayHandle: Boolean? = null

    private var closeIconButton: IconButton? = null
    private var iconButtons: Array<IconButton?> = arrayOfNulls(ICON_BUTTONS_AMOUNT_MAX)

    private var titleText: String? = null

    @ColorInt
    private var titleColor: Int? = null

    protected var positiveText: String? = null
    private var negativeText: String? = null

    @DrawableRes
    protected var positiveButtonDrawableRes: Int? = null

    @DrawableRes
    private var negativeButtonDrawableRes: Int? = null

    private var customLayoutHeight: Int? = null

    /** Set the top style. */
    fun topStyle(topStyle: TopStyle) {
        this.topStyle = topStyle
    }

    /** Set the negative button style. */
    fun negativeButtonStyle(negativeButtonStyle: ButtonStyle) {
        this.negativeButtonStyle = negativeButtonStyle
    }

    /** Set the positive button style. */
    fun positiveButtonStyle(positiveButtonStyle: ButtonStyle) {
        this.positiveButtonStyle = positiveButtonStyle
    }

    /** Set the negative button color. */
    fun negativeButtonColorRes(@ColorRes negativeButtonColor: Int) {
        this.negativeButtonColor = ContextCompat.getColor(windowContext, negativeButtonColor)
    }

    /** Set the positive button color. */
    fun positiveButtonColorRes(@ColorRes positiveButtonColor: Int) {
        this.positiveButtonColor = ContextCompat.getColor(windowContext, positiveButtonColor)
    }

    /** Set the negative button color. */
    fun negativeButtonColor(@ColorInt negativeButtonColor: Int) {
        this.negativeButtonColor = negativeButtonColor
    }

    /** Set the positive button color. */
    fun positiveButtonColor(@ColorInt positiveButtonColor: Int) {
        this.positiveButtonColor = positiveButtonColor
    }

    /** Set a cover image. */
    fun withCoverImage(image: Image) {
        this.coverImage = image
        this.useCover = true
    }

    /** Set the Close Button Drawable. */
    fun closeIconButton(iconButton: IconButton) {
        this.closeIconButton = iconButton
    }

    /** Display the negative button. */
    fun displayNegativeButton(displayNegativeButton: Boolean) {
        this.displayNegativeButton = displayNegativeButton
    }

    /** Display the positive button. */
    fun displayPositiveButton(displayPositiveButton: Boolean) {
        this.displayPositiveButton = displayPositiveButton
    }

    /**
     * Display the toolbar. The toolbar consists of the close icon button,
     * the title, the divider and the optional icon buttons.
     * Overrides the style default.
     */
    fun displayToolbar(displayToolbar: Boolean = true) {
        this.displayToolbar = displayToolbar
    }

    /**
     * Display the close button left of the title.
     * Overrides the style default.
     */
    fun displayCloseButton(displayCloseButton: Boolean = true) {
        this.displayCloseButton = displayCloseButton
    }

    /**
     * Display the handle.
     * Overrides the style default.
     */
    fun displayHandle(displayHandle: Boolean = true) {
        this.displayHandle = displayHandle
    }

    /**
     * Set the title of the sheet.
     *
     * @param title The text for the title.
     */
    fun title(title: String) {
        this.titleText = title
    }

    /**
     * Set the title of the sheet.
     *
     * @param titleRes The String resource id for the title.
     */
    fun title(@StringRes titleRes: Int) {
        this.titleText = windowContext.getString(titleRes)
    }

    /**
     * Set the title of the sheet.
     *
     * @param titleRes Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *                   substitution.
     */
    fun title(@StringRes titleRes: Int, vararg formatArgs: Any?) {
        this.titleText = windowContext.getString(titleRes, *formatArgs)
    }

    /**
     * Set the title color of the sheet.
     *
     * @param titleColorRes Color res for the title.
     */
    fun titleColorRes(@ColorRes titleColorRes: Int) {
        this.titleColor = ContextCompat.getColor(windowContext, titleColorRes)
    }

    /**
     * Set the title color of the sheet.
     *
     * @param titleColor Color for the title.
     */
    fun titleColor(@ColorInt titleColor: Int) {
        this.titleColor = titleColor
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
        this.negativeButtonDrawableRes = drawableRes
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
        this.negativeButtonDrawableRes = drawableRes
        this.negativeListener = negativeListener
    }

    /** Set a listener that is invoked when the sheet is dismissed (after negative or positive button click). */
    fun onDismiss(dismissListener: DismissListener) {
        this.dismissListener = dismissListener
    }

    /** Set a listener that is invoked when the sheet is cancelled. */
    fun onCancel(cancelListener: CancelListener) {
        this.cancelListener = cancelListener
    }

    /** Set a listener that is invoked when the sheet is closed. */
    fun onClose(closeListener: CloseListener) {
        this.closeListener = closeListener
    }

    /** Set the height of the custom sheet layout. */
    fun customLayoutHeight(customLayoutHeight: Int) {
        this.customLayoutHeight = customLayoutHeight
    }

    override fun dismiss() {
        super.dismiss()
        this.dismissListener?.invoke()
        this.closeListener?.invoke()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        this.cancelListener?.invoke()
        this.closeListener?.invoke()
    }

    /**
     * Add an icon button to the sheet toolbar to the right of the title.
     * The icon buttons will be aligned from the right in the order you call this method.
     * You can only add up to 3 icon buttons.
     * @throws IllegalStateException If you add more than 3 icon buttons.
     */
    fun withIconButton(iconButton: IconButton, listener: ClickListener? = null) {
        val index = this.iconButtons.indexOfFirst { it == null }
        if (index == -1) throw IllegalStateException("You can only add 3 icon buttons.")
        this.iconButtons[index] = iconButton.apply { listener(listener) }
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getTopStyle(): TopStyle {
        return topStyle
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun useCover(useCover: Boolean = true) {
        this.useCover = useCover
    }

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun <T> getCoverAnimationView(): T? {
        return coverAnimationView as T?
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun setCoverAnimationView(coverAnimationView: Any) {
        this.coverAnimationView = coverAnimationView
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun addOnCreateViewListener(onCreateViewListeners: OnViewCreatedListener) {
        this.onCreateViewListeners.add(onCreateViewListeners)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun removeOnCreateViewListener(onCreateViewListener: OnViewCreatedListener) {
        this.onCreateViewListeners.remove(onCreateViewListener)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun addAddOnComponent(addOnComponent: AddOnComponent) {
        this.addOnComponents.add(addOnComponent)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun removeAddOnComponent(addOnComponent: AddOnComponent) {
        this.addOnComponents.remove(addOnComponent)
    }

    private fun isLandscapeMode(): Boolean {
        return this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /** Create view of base sheet. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?,
    ): View? {
        if (saved != null) {
            dismiss()
            return null
        }
        base = SheetsBaseBinding.inflate(LayoutInflater.from(activity), container, false)
        val layout = onCreateLayoutView()
        customLayoutHeight?.let { base.layout.layoutParams.apply { height = it } }
        base.layout.addView(layout)
        return base.root
    }

    /** Create custom view to be added to the base sheet. */
    abstract fun onCreateLayoutView(): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {

        val iconsColor = getIconColor(requireContext())

        val isHandleVisible =
            displayHandle ?: isDisplayHandle(requireContext(), DEFAULT_DISPLAY_HANDLE)

        val isToolbarSetup = titleText != null && iconButtons.isNotEmpty()
        val isToolbarVisible = displayToolbar ?: isDisplayToolbar(requireContext(), isToolbarSetup)

        val isCloseButtonVisible = sheetStyle == SheetStyle.BOTTOM_SHEET &&
                displayCloseButton ?: isDisplayCloseButton(
            requireContext(),
            DEFAULT_DISPLAY_CLOSE_BUTTON
        )

        with(base) {
            handle.visibility = if (isHandleVisible) View.VISIBLE else View.GONE
            top.root.visibility = if (isToolbarVisible) View.VISIBLE else View.GONE
            top.btnClose.visibility =
                if (isToolbarVisible && isCloseButtonVisible) View.VISIBLE else View.GONE
            if (isToolbarVisible) {
                setupTopBar()
                titleText?.let { top.title.text = it }
                titleColor?.let { top.title.setTextColor(it) }
                if (isCloseButtonVisible) {
                    closeIconButton?.let { btn ->
                        val drawable =
                            btn.drawableRes?.let { ContextCompat.getDrawable(requireContext(), it) }
                                ?: btn.drawable
                        top.btnClose.setImageDrawable(drawable)
                        top.btnClose.setColorFilter(btn.drawableColor?.let {
                            ContextCompat.getColor(
                                requireContext(),
                                it
                            )
                        } ?: iconsColor)
                    }
                    top.btnClose.setOnClickListener { dismiss() }
                }
            }

            top.btnType.setColorFilter(iconsColor)
            top.btnExtra1.setColorFilter(iconsColor)
            top.btnExtra2.setColorFilter(iconsColor)
            top.btnExtra3.setColorFilter(iconsColor)
        }

        setupIconButtons()
        setupButtonsView()

        onCreateViewListeners.forEach { listener -> listener(base) }
    }

    private fun setupTopBar() {

        // Do not display a cover, and therefore also no special TopStyle,
        // in landscape mode, due the limited height.
        if (isLandscapeMode()) {
            return
        }

        if (useCover) {
            setupTopStyle()
        }

        coverImage?.let { source ->
            setupCoverSource(source, base.top.coverImage)
            base.top.coverImage.loadAny(coverImage?.any) {
                source.coilRequestBuilder.invoke(this)
            }
        }
    }

    /**
     * Setup an image source for the cover view.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun setupCoverSource(imageSource: ImageSource, imageView: AppCompatImageView) {

        // Do not display a cover in landscape mode, due the limited height.
        if (isLandscapeMode()) {
            return
        }

        imageSource.ratio?.dimensionRatio?.let {
            (base.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                dimensionRatio = it
            }
        }

        imageSource.imageViewBuilder?.invoke(imageView)
        base.top.cover.visibility = View.VISIBLE
    }

    private fun setupTopStyle() {

        if (topStyle != TopStyle.ABOVE_COVER) {

            val cornerFamily =
                cornerFamily ?: getCornerFamily(requireContext()) ?: DEFAULT_CORNER_FAMILY

            val cornerRadius =
                cornerRadiusDp?.toDp() ?: getCornerRadius(requireContext())
                ?: DEFAULT_CORNER_RADIUS.toDp()

            base.top.coverImage.shapeAppearanceModel =
                ShapeAppearanceModel().toBuilder().apply {
                    setTopRightCorner(cornerFamily, cornerRadius)
                    setTopLeftCorner(cornerFamily, cornerRadius)
                }.build()
        }

        when (topStyle) {

            TopStyle.ABOVE_COVER -> {
                /* Standard */
            }

            TopStyle.MIXED -> {

                (base.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = base.top.title.id
                }

                (base.top.title.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = base.top.divider.id
                    topToBottom = base.top.cover.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    setMargins(16.toDp(), 0, 0, 0)
                }

                (base.top.btnType.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = ConstraintLayout.LayoutParams.UNSET
                    bottomToBottom = base.top.guideline.id
                }

                (base.top.btnClose.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                }
            }

            TopStyle.BELOW_COVER -> {

                (base.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = base.top.title.id
                }

                (base.top.title.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = base.top.divider.id
                    topToBottom = base.top.cover.id
                }

                (base.top.btnType.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.UNSET
                    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    bottomToTop = base.top.divider.id
                    topToBottom = base.top.cover.id
                }

                (base.top.btnClose.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = base.top.divider.id
                    topToBottom = base.top.cover.id
                    topToTop = ConstraintLayout.LayoutParams.UNSET
                }
            }
        }
    }

    private fun setupIconButtons() {

        iconButtons.filterNotNull().forEachIndexed { i, btn ->
            btn.drawable?.let { setToolbarExtraButtonDrawable(i, it) }
            btn.drawableRes?.let { setToolbarExtraButtonDrawable(i, it) }
            btn.drawableColor?.let { setToolbarExtraButtonColor(i, it) }
            btn.listener?.let { listener -> setToolbarExtraButtonListener(i, listener) }
            displayToolbarExtraButton(i)
        }
    }

    private fun setToolbarExtraButtonDrawable(i: Int, @DrawableRes drawableRes: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableRes)
        setToolbarExtraButtonDrawable(i, drawable)
    }

    private fun setToolbarExtraButtonColor(i: Int, @ColorRes color: Int) {
        with(base.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setColorFilter(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun setToolbarExtraButtonDrawable(i: Int, drawable: Drawable?) {
        with(base.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setImageDrawable(drawable)
        }
    }

    private fun displayToolbarExtraButton(i: Int) {
        with(base.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.visibility = View.VISIBLE
        }
    }

    private fun setupButtonsView() {

        if (displayNegativeButton) {
            base.buttons.btnNegativeContainer.setupNegativeButton(
                buttonStyle = negativeButtonStyle,
                buttonColor = negativeButtonColor,
                btnText = negativeText ?: getString(android.R.string.cancel),
                btnDrawable = negativeButtonDrawableRes
            ) { negativeListener?.invoke(); dismiss() }
        }

        if (displayPositiveButton) {
            base.buttons.btnPositiveContainer.setupPositiveButton(
                buttonStyle = positiveButtonStyle,
                buttonColor = positiveButtonColor,
                btnText = positiveText ?: getString(android.R.string.ok),
                btnDrawable = positiveButtonDrawableRes
            ) { positiveListener?.invoke(); dismiss() }
        }
    }

    /** Display the positive button. */
    protected fun displayButtonPositive(display: Boolean, fade: Boolean = true) {
        base.buttons.btnPositiveContainer.apply {
            positiveButtonClickable(display)
            if (display) showButtonPositive(fade) else hideButtonPositive(fade)
        }
    }

    /** Show positive button */
    private fun showButtonPositive(fade: Boolean) {
        base.buttons.btnPositiveContainer.run { if (fade) fadeIn() else fadeIn(duration = 0) }
        base.buttons.btnPositiveContainer.isClickable = true
    }

    /** Hide positive button. */
    private fun hideButtonPositive(fade: Boolean) {
        base.buttons.btnPositiveContainer.run { if (fade) fadeOut() else fadeOut(duration = 0) }
        base.buttons.btnPositiveContainer.isClickable = false
    }

    /** Display buttons view. */
    protected fun displayButtonsView(display: Boolean) {
        base.buttons.root.visibility = if (display) View.VISIBLE else View.GONE
    }

    /** Set a listener which is invoked when the positive button is clicked. */
    protected fun setButtonPositiveListener(listener: ClickListener) {
        base.buttons.btnPositiveContainer.positiveButtonListener(listener)
    }

    /** Set a listener which is invoked when the type icon button is clicked. */
    protected fun setToolbarTypeButtonListener(listener: ClickListener) {
        base.top.btnType.setOnClickListener { listener.invoke() }
    }

    /** Set a drawable for the type icon button. */
    protected fun setToolbarTypeButtonDrawable(@DrawableRes drawableRes: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableRes)
        base.top.btnType.setImageDrawable(drawable)
        base.top.btnType.visibility = View.VISIBLE
    }

    /** Display the type icon button. */
    protected fun displayToolbarTypeButton(display: Boolean) {
        base.top.btnType.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun setToolbarExtraButtonListener(i: Int, listener: ClickListener) {
        with(base.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setOnClickListener { listener.invoke() }
        }
    }
}
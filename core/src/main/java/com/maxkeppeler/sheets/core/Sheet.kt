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

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.loadAny
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.sheets.R
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.databinding.SheetsBaseBinding
import java.io.Serializable

/** Listener which is invoked when the positive button is clicked. */
typealias PositiveListener = () -> Unit

/** Listener which is invoked when the negative button is clicked. */
typealias NegativeListener = () -> Unit

/** Listener which is invoked when the sheet is dismissed. */
typealias DismissListener = () -> Unit

/** Listener which is invoked when buttons are clicked. */
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
        const val DEFAULT_DISPLAY_TOOLBAR = true
        const val DEFAULT_DISPLAY_CLOSE_BUTTON = true
        const val ICON_BUTTONS_AMOUNT_MAX = 3
        private const val STATE_BASE_ADD_ON_RECEIVER = "state_base_add_on_receiver"
        private const val STATE_BASE_ADD_ON_RECEIVER_AMOUNT = "state_base_add_on_receiver_amount"
        private const val STATE_BASE_SHEET_STYLE = "state_base_sheet_style"
        private const val STATE_BASE_DISPLAY_TOOLBAR = "state_base_display_toolbar"
        private const val STATE_BASE_TOP_STYLE = "state_top_style"
        private const val STATE_BASE_COVER_IMAGE = "state_cover_image"
        private const val STATE_BASE_DISPLAY_CLOSE_BUTTON = "state_base_display_close_button"
        private const val STATE_BASE_DISPLAY_HANDLE = "state_base_display_handle"
        private const val STATE_BASE_TITLE_TEXT = "state_base_title_text"
        private const val STATE_BASE_CLOSE_ICON_BUTTON = "state_base_close_icon_button"
        private const val STATE_BASE_POSITIVE_TEXT = "state_base_positive_text"
        private const val STATE_BASE_NEGATIVE_TEXT = "state_base_negative_text"
        private const val STATE_BASE_POSITIVE_BUTTON_DRAWABLE =
            "state_base_positive_button_drawable"
        private const val STATE_BASE_NEGATIVE_BUTTON_DRAWABLE =
            "state_base_negative_button_drawable"
        private const val STATE_BASE_DISMISS_LISTENER = "state_base_dismiss_listener"
        private const val STATE_BASE_POSITIVE_LISTENER = "state_base_positive_listener"
        private const val STATE_BASE_NEGATIVE_LISTENER = "state_base_negative_listener"
        private const val STATE_BASE_ICON_BUTTONS = "state_base_icon_buttons"
    }

    private var addOnComponents = mutableListOf<AddOnComponent>()
    private var onCreateViewListeners = mutableListOf<OnViewCreatedListener>()

    private var topStyle = TopStyle.ABOVE_COVER

    private var useCover: Boolean = false
    private var coverImage: Image? = null
    private var coverAnimationView: Any? = null

    private lateinit var bindingBase: SheetsBaseBinding

    private var displayToolbar: Boolean? = null
    private var displayCloseButton: Boolean? = null
    private var displayHandle: Boolean? = null

    private var titleText: String? = null

    private var closeIconButton: IconButton? = null

    protected var positiveText: String? = null
    private var negativeText: String? = null

    @DrawableRes
    protected var positiveButtonDrawableRes: Int? = null

    @DrawableRes
    private var negativeButtonDrawableRes: Int? = null

    private var dismissListener: DismissListener? = null
    protected var positiveListener: PositiveListener? = null
    private var negativeListener: NegativeListener? = null

    private var iconButtons: Array<IconButton?> = arrayOfNulls(ICON_BUTTONS_AMOUNT_MAX)

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

    /** Set a cover image. */
    fun withCoverImage(image: Image) {
        this.coverImage = image
        this.useCover = true
    }

    /** Set the top style. */
    fun topStyle(topStyle: TopStyle) {
        this.topStyle = topStyle
    }

    /** Set if sheet is cancelable outside. */
    fun cancelableOutside(cancelable: Boolean) {
        this.isCancelable = cancelable
    }

    /** Set the Close Button Drawable. */
    fun closeIconButton(iconButton: IconButton) {
        this.closeIconButton = iconButton
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

    /** Sets a listener that is invoked when the sheet is dismissed. */
    fun onDismiss(dismissListener: DismissListener? = null) {
        this.dismissListener = dismissListener
    }

    /** Override dismiss to trigger custom dismiss listener. */
    override fun dismiss() {
        super.dismiss()
        dismissListener?.invoke()
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

    /** Create view of base sheet. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View? {
        if (saved?.isEmpty == true) dismiss()
        return SheetsBaseBinding.inflate(LayoutInflater.from(activity), container, false)
            .also { bindingBase = it }.apply {
                layout.addView(onCreateLayoutView())
            }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onRestoreBaseViewInstanceState(savedInstanceState)
        onRestoreCustomViewInstanceState(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    private fun onRestoreBaseViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            titleText = saved.getString(STATE_BASE_TITLE_TEXT)
            positiveText = saved.getString(STATE_BASE_POSITIVE_TEXT)
            negativeText = saved.getString(STATE_BASE_NEGATIVE_TEXT)
            displayToolbar = saved.get(STATE_BASE_DISPLAY_TOOLBAR) as Boolean?
            displayCloseButton = saved.get(STATE_BASE_DISPLAY_CLOSE_BUTTON) as Boolean?
            displayHandle = saved.get(STATE_BASE_DISPLAY_HANDLE) as Boolean?
            negativeButtonDrawableRes = saved.get(STATE_BASE_NEGATIVE_BUTTON_DRAWABLE) as Int?
            positiveButtonDrawableRes = saved.get(STATE_BASE_POSITIVE_BUTTON_DRAWABLE) as Int?
            dismissListener = saved.getSerializable(STATE_BASE_DISMISS_LISTENER) as DismissListener?
            negativeListener =
                saved.getSerializable(STATE_BASE_NEGATIVE_LISTENER) as NegativeListener?
            positiveListener =
                saved.getSerializable(STATE_BASE_POSITIVE_LISTENER) as PositiveListener?
             topStyle = saved.getSerializable(STATE_BASE_TOP_STYLE) as TopStyle
            coverImage = saved.getSerializable(STATE_BASE_COVER_IMAGE) as Image?
            closeIconButton = saved.getSerializable(STATE_BASE_CLOSE_ICON_BUTTON) as IconButton?
            val icons = mutableListOf<IconButton>()
            repeat(ICON_BUTTONS_AMOUNT_MAX) {
                val iconButton =
                    saved.getSerializable(STATE_BASE_ICON_BUTTONS.plus(it)) as IconButton?
                iconButton?.let { btn -> icons.add(btn) }
            }
            iconButtons = icons.toTypedArray()
            val addOnReceiversAmount = saved.getInt(STATE_BASE_ADD_ON_RECEIVER_AMOUNT)
            repeat(addOnReceiversAmount) {
                val receiver =
                    saved.getSerializable(STATE_BASE_ADD_ON_RECEIVER.plus(it)) as AddOnComponent?
                receiver?.let { rec ->
                    addOnComponents.add(rec)
                    rec.invoke(this)
                }
            }
        }
    }

    abstract fun onRestoreCustomViewInstanceState(savedState: Bundle?)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveBaseViewInstanceState(outState)
        onSaveCustomViewInstanceState(outState)
    }

    private fun onSaveBaseViewInstanceState(outState: Bundle) {
        with(outState) {
            putString(STATE_BASE_TITLE_TEXT, titleText)
            putString(STATE_BASE_POSITIVE_TEXT, positiveText)
            putString(STATE_BASE_NEGATIVE_TEXT, negativeText)
            displayToolbar?.let { putBoolean(STATE_BASE_DISPLAY_TOOLBAR, it) }
            displayCloseButton?.let { putBoolean(STATE_BASE_DISPLAY_CLOSE_BUTTON, it) }
            displayHandle?.let { putBoolean(STATE_BASE_DISPLAY_HANDLE, it) }
            negativeButtonDrawableRes?.let { putInt(STATE_BASE_NEGATIVE_BUTTON_DRAWABLE, it) }
            positiveButtonDrawableRes?.let { putInt(STATE_BASE_POSITIVE_BUTTON_DRAWABLE, it) }
              putSerializable(STATE_BASE_TOP_STYLE, topStyle)
            putSerializable(STATE_BASE_COVER_IMAGE, coverImage)
            putSerializable(STATE_BASE_CLOSE_ICON_BUTTON, closeIconButton)
            iconButtons.forEachIndexed { i, btn ->
                putSerializable(STATE_BASE_ICON_BUTTONS.plus(i), btn)
            }
            putSerializable(STATE_BASE_DISMISS_LISTENER, dismissListener as Serializable?)
            putSerializable(STATE_BASE_NEGATIVE_LISTENER, negativeListener as Serializable?)
            putSerializable(STATE_BASE_POSITIVE_LISTENER, positiveListener as Serializable?)
            putInt(STATE_BASE_ADD_ON_RECEIVER_AMOUNT, addOnComponents.size)
            addOnComponents.forEachIndexed { i, function ->
                putSerializable(STATE_BASE_ADD_ON_RECEIVER.plus(i), function as Serializable?)
            }
        }
    }

    abstract fun onSaveCustomViewInstanceState(outState: Bundle)

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

        val isToolbarVisible =
            displayToolbar ?: isDisplayToolbar(requireContext(), DEFAULT_DISPLAY_TOOLBAR)

        val isCloseButtonVisible = sheetStyle == SheetStyle.BOTTOM_SHEET &&
            displayCloseButton ?: isDisplayCloseButton(
                requireContext(),
                DEFAULT_DISPLAY_CLOSE_BUTTON
            )

        with(bindingBase) {
            handle.visibility = if (isHandleVisible) View.VISIBLE else View.GONE
            top.root.visibility = if (isToolbarVisible) View.VISIBLE else View.GONE
            top.btnClose.visibility =
                if (isToolbarVisible && isCloseButtonVisible) View.VISIBLE else View.GONE
            if (isToolbarVisible) {
                setupTopBar()
                titleText?.let { top.title.text = it }
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

        onCreateViewListeners.forEach { listener -> listener(bindingBase) }
    }

    private fun setupTopBar() {

        if (useCover && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setupTopStyle()
        }

        coverImage?.let { source ->
            setupCoverSource(source)
            bindingBase.top.coverImage.loadAny(coverImage?.any) {
                source.coilRequestBuilder.invoke(this)
            }
        }
    }

    /**
     * Setup an image source for the cover view.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun setupCoverSource(imageSource: ImageSource) {

        imageSource.ratio?.dimensionRatio?.let {
            (bindingBase.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                dimensionRatio = it
            }
        }

        imageSource.scaleType?.let {
            bindingBase.top.coverImage.scaleType = it
        }

        bindingBase.top.cover.visibility = View.VISIBLE
    }

    private fun setupTopStyle() {

        if (topStyle != TopStyle.ABOVE_COVER) {

            val cornerFamily =
                cornerFamily ?: getCornerFamily(requireContext()) ?: DEFAULT_CORNER_FAMILY

            val cornerRadius =
                cornerRadiusDp?.toDp() ?: getCornerRadius(requireContext()) ?: DEFAULT_CORNER_RADIUS.toDp()

            bindingBase.top.coverImage.shapeAppearanceModel =
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

                (bindingBase.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bindingBase.top.title.id
                }

                (bindingBase.top.title.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = bindingBase.top.divider.id
                    topToBottom = bindingBase.top.cover.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    setMargins(16.toDp(), 0, 0, 0)
                }

                (bindingBase.top.btnType.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = ConstraintLayout.LayoutParams.UNSET
                    bottomToBottom = bindingBase.top.guideline.id
                }

                (bindingBase.top.btnClose.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                }
            }

            TopStyle.BELOW_COVER -> {

                (bindingBase.top.cover.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bindingBase.top.title.id
                }

                (bindingBase.top.title.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = bindingBase.top.divider.id
                    topToBottom = bindingBase.top.cover.id
                }

                (bindingBase.top.btnType.layoutParams as ConstraintLayout.LayoutParams).apply {
                    topToTop = ConstraintLayout.LayoutParams.UNSET
                    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    bottomToTop = bindingBase.top.divider.id
                    topToBottom = bindingBase.top.cover.id
                }

                (bindingBase.top.btnClose.layoutParams as ConstraintLayout.LayoutParams).apply {
                    bottomToTop = bindingBase.top.divider.id
                    topToBottom = bindingBase.top.cover.id
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
        with(bindingBase.top) {
            when (i) {
                0 -> btnExtra1
                1 -> btnExtra2
                else -> btnExtra3
            }.setColorFilter(ContextCompat.getColor(requireContext(), color))
        }
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
            btnDrawable = negativeButtonDrawableRes
        ) { negativeListener?.invoke(); dismiss() }

        bindingBase.buttons.btnPositiveContainer.setupPositiveButton(
            btnText = positiveText ?: getString(R.string.ok),
            btnDrawable = positiveButtonDrawableRes
        ) { positiveListener?.invoke(); dismiss() }
    }

    /** Display the positive button. */
    protected fun displayButtonPositive(display: Boolean) {
        bindingBase.buttons.btnPositiveContainer.apply {
            positiveButtonClickable(display)
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
}
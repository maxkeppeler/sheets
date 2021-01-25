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

package com.maxkeppeler.sheets.options

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.layoutmanagers.CustomGridLayoutManager
import com.maxkeppeler.sheets.core.layoutmanagers.CustomLinearLayoutManager
import com.maxkeppeler.sheets.core.utils.getPrimaryColor
import com.maxkeppeler.sheets.options.databinding.SheetsOptionsBinding
import java.io.Serializable

/** Listener which returns the selected index and the respective option.
 * If multiple choices is enabled, this listener will return nothing. Use the [OptionsListener]. */
typealias OptionListener = (index: Int, option: Option) -> Unit

/** Listener which returns the selected option's index and text. */
typealias OptionsListener = OptionsSheet.(selectedIndices: MutableList<Int>, selectedOptions: MutableList<Option>) -> Unit

/**
 * The [OptionsSheet] lets you display a grid or list of options.
 */
class OptionsSheet : Sheet() {

    override val dialogTag = "OptionsSheet"

    companion object {
        private const val SMALL_GRID_ITEMS_MAX = 8
        private const val GRID_COLUMNS_MAX = 4
        private const val STATE_LISTENER = "state_listener"
        private const val STATE_LISTENER_MULTIPLE = "state_listener_multiple"
        private const val STATE_OPTIONS_AMOUNT = "state_options_amount"
        private const val STATE_OPTIONS = "state_options"
        private const val STATE_OPTIONS_SELECTED = "state_options_selected"
        private const val STATE_MODE = "state_mode"
        private const val STATE_MULTIPLE_CHOICES_INFO = "state_multiple_choices"
        private const val STATE_MULTIPLE_CHOICES = "state_multiple_choices_info"
        private const val STATE_MIN_CHOICES = "state_min_choices"
        private const val STATE_MAX_CHOICES = "state_max_choices"
        private const val STATE_MAX_CHOICES_STRICT = "state_max_choices_strict"
        private const val STATE_DISPLAY_BUTTONS = "state_display_buttons"
    }

    private lateinit var binding: SheetsOptionsBinding
    private lateinit var optionsAdapter: OptionsAdapter

    private var colorActive: Int = 0

    private var listener: OptionListener? = null
    private var listenerMultiple: OptionsListener? = null
    private var options = mutableListOf<Option>()
    private var optionsSelected = mutableListOf<Int>()

    private var mode = DisplayMode.GRID_HORIZONTAL
    private var multipleChoices = false
    private var displayMultipleChoicesInfo = false
    private var minChoices: Int? = null
    private var maxChoices: Int? = null
    private var maxChoicesStrict = true
    private var displayButtons = false

    private val saveAllowed: Boolean
        get() {
            val validMultipleChoice =
                multipleChoices && optionsSelected.size >= minChoices ?: 0
                        && optionsSelected.size <= maxChoices ?: options.size
            val validSingleChoice = !multipleChoices && optionsSelected.isNotEmpty()
            return validMultipleChoice || validSingleChoice
        }


    /** Show buttons and require a positive button click. */
    fun multipleChoices(multipleChoices: Boolean = true) {
        this.multipleChoices = multipleChoices
    }

    /** Display the hints for allowed minimum and maximum amount of choices and the amount of selected options. */
    fun displayMultipleChoicesInfo(displayMultipleChoicesInfo: Boolean = true) {
        this.displayMultipleChoicesInfo = displayMultipleChoicesInfo
    }

    /** A strict limit for the maximum amount of choices will prevent the user to select more than allowed.  */
    fun maxChoicesStrictLimit(maxChoicesStrict: Boolean = true) {
        this.maxChoicesStrict = maxChoicesStrict
    }

    /** Set the minimum amount of (enabled) choices. */
    fun minChoices(@IntRange(from = 1) minChoices: Int) {
        maxChoices?.let { max ->
            if (minChoices > max)
                throw IllegalStateException("The minimum amount of selected options needs to be less or equal to the maximum amount.")
        }
        this.minChoices = minChoices
    }

    /** Set the maximum amount of (enabled) choices. */
    fun maxChoices(maxChoices: Int) {
        minChoices?.let { min ->
            if (maxChoices < min)
                throw IllegalStateException("The maximum amount of selected options needs to be more or equal to the minimum amount.")
        }
        this.maxChoices = maxChoices
    }

    /** Display buttons and require a positive button click. */
    fun displayButtons(displayButtons: Boolean = true) {
        this.displayButtons = displayButtons
    }

    /** Set display mode. */
    fun displayMode(displayMode: DisplayMode) {
        this.mode = displayMode
    }

    /**
     * Set the [OptionListener].
     *
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositive(listener: OptionListener) {
        this.listener = listener
    }

    /**
     * Set the text of the positive button and set the [OptionListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, listener: OptionListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = listener
    }

    /**
     *  Set the text of the positive button and set the [OptionListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositive(positiveText: String, listener: OptionListener? = null) {
        this.positiveText = positiveText
        this.listener = listener
    }

    /**
     * Set the text and icon of the positive button and set the [OptionListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: OptionListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [OptionListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: OptionListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listener = listener
    }

    /**
     * Set the [OptionListener].
     *
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositiveMultiple(listener: OptionsListener) {
        this.listenerMultiple = listener
    }

    /**
     * Set the text of the positive button and set the [OptionListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositiveMultiple(@StringRes positiveRes: Int, listener: OptionsListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listenerMultiple = listener
    }

    /**
     *  Set the text of the positive button and set the [OptionListener].
     *
     * @param positiveText The text for the positive button.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositiveMultiple(positiveText: String, listener: OptionsListener? = null) {
        this.positiveText = positiveText
        this.listenerMultiple = listener
    }

    /**
     * Set the text and icon of the positive button and set the [OptionListener].
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositiveMultiple(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        listener: OptionsListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.listenerMultiple = listener
    }

    /**
     *  Set the text and icon of the positive button and set the [OptionListener].
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param listener Listener that is invoked with the selected options when the positive button is clicked.
     */
    fun onPositiveMultiple(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        listener: OptionsListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listenerMultiple = listener
    }

    /**
     * Add multiple options.
     *
     * @param options The [Option] arguments to be added.
     */
    fun with(vararg options: Option) {
        this.options.addAll(options.toMutableList())
    }

    /**
     * Add multiple options as a MutableList
     *
     * @param options The [Option] arguments to be added.
     */
    fun with(options: MutableList<Option>) {
        this.options.addAll(options)
    }

    /**
     * Add an option.
     *
     * @param option Instance of [Option].
     */
    fun with(option: Option) {
        this.options.add(option)
    }

    private val adapterListener = object : OptionsSelectionListener {

        override fun select(index: Int) {
            if (displayButtons) {
                optionsSelected.clear()
                optionsSelected.add(index)
                validate()
            } else {
                optionsSelected.add(index)
                Handler(Looper.getMainLooper()).postDelayed({
                    listener?.invoke(index, options[index])
                    dismiss()
                }, 300)
            }
        }

        override fun isSelected(index: Int): Boolean =
            optionsSelected.contains(index)

        override fun selectMultipleChoice(index: Int) {
            if (!optionsSelected.contains(index)) {
                optionsSelected.add(index)
                validate()
            }
        }

        override fun deselectMultipleChoice(index: Int) {
            optionsSelected.remove(index)
            validate()
        }

        override fun isMultipleChoiceSelectionAllowed(index: Int): Boolean =
            (multipleChoices && optionsSelected.contains(index) ||
                    (maxChoicesStrict && optionsSelected.size < maxChoices ?: options.size)
                    || (!maxChoicesStrict && optionsSelected.size < options.size))
    }

    override fun onCreateLayoutView(): View =
        SheetsOptionsBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSetup()
        displayButtonsView(multipleChoices || displayButtons)
        setButtonPositiveListener(::save)

        colorActive = getPrimaryColor(requireContext())

        with(binding.optionsRecyclerView) {

            val columns = if (options.size < GRID_COLUMNS_MAX) options.size else GRID_COLUMNS_MAX
            val collapsedItems = when (mode) {
                DisplayMode.GRID_HORIZONTAL -> options.size <= SMALL_GRID_ITEMS_MAX
                DisplayMode.GRID_VERTICAL -> true
                DisplayMode.LIST -> false
            }

            optionsAdapter =
                OptionsAdapter(
                    requireActivity(),
                    options,
                    mode,
                    multipleChoices,
                    collapsedItems,
                    adapterListener
                )
            adapter = optionsAdapter

            setItemViewCacheSize(options.size)

            layoutManager = when (mode) {

                DisplayMode.GRID_HORIZONTAL -> if (collapsedItems) CustomGridLayoutManager(
                    requireContext(),
                    columns,
                    false
                )
                else CustomLinearLayoutManager(requireContext(), true, RecyclerView.HORIZONTAL)

                DisplayMode.GRID_VERTICAL -> CustomGridLayoutManager(
                    requireContext(),
                    columns,
                    true
                )

                DisplayMode.LIST -> CustomLinearLayoutManager(requireContext(), true)
            }
        }

        validate()
    }

    /** Validate if the current selections fulfils the requirements. */
    private fun validate() {

        displayButtonPositive(saveAllowed)

        if (multipleChoices) {
            updateMultipleChoicesInfo()
        }
    }

    /** Return the options and dismiss dialog. */
    private fun save() {

        if (multipleChoices) {
            val selectedOptions =
                options.filterIndexed { index, _ -> optionsSelected.contains(index) }
                    .toMutableList()
            listenerMultiple?.invoke(this, optionsSelected, selectedOptions)
        } else {
            optionsSelected.firstOrNull()?.let { index ->
                listener?.invoke(index, options[index])
            }
        }
        dismiss()
    }

    private fun checkSetup() {

        if (options.isEmpty())
            throw IllegalStateException("No options added.")

        checkChoicesSetup()
    }

    private fun checkChoicesSetup() {

        if (!multipleChoices) {

            if (options.any { it.selected && it.disabled }) {
                throw IllegalStateException("An option is already selected and can't be changed because it's disabled. ")
            }
        }

        if (minChoices != null && maxChoices != null) {
            binding.range.minimumLabel.visibility = View.GONE
            binding.range.selectionStatus.visibility = View.VISIBLE
        }

        if (options.all { it.disabled })
            throw IllegalStateException("All options are disabled.")

        minChoices?.let { min ->

            if (min >= options.size)
                throw IllegalStateException("Minimum amount of choices exceed the amount of options.")

            if (min >= options.filterNot { it.disabled && !it.selected }.size)
                throw IllegalStateException("Minimum amount of choices exceed the amount of enabled options.")
        }

        maxChoices?.let { max ->

            if (max > options.size)
                throw IllegalStateException("Maximum amount of choices exceed the amount of options.")

            if (max > options.filterNot { it.disabled && !it.selected }.size)
                throw IllegalStateException("Maximum amount of choices exceed the amount of enabled options.")
        }
    }

    private fun updateMultipleChoicesInfo() {

        if (!displayMultipleChoicesInfo) {
            binding.range.root.visibility = View.GONE
            return
        }

        binding.range.root.visibility = View.VISIBLE

        val selected = optionsSelected.size

        val isMinLabelShown = minChoices?.let { min ->
            val lessThanSelected = selected < min
            if (lessThanSelected) {
                binding.range.minimumLabel.text = getString(R.string.select_at_least, min)
                binding.range.minimumLabel.visibility = View.VISIBLE
            } else {
                binding.range.minimumLabel.visibility = View.GONE
            }
            lessThanSelected
        } ?: false

        maxChoices?.let { max ->
            if (!maxChoicesStrict && selected > max) {
                binding.range.minimumLabel.text = getString(R.string.select_at_most, max)
                binding.range.minimumLabel.visibility = View.VISIBLE
            } else if (!isMinLabelShown) {
                binding.range.minimumLabel.visibility = View.GONE
            }
        }

        val actualMaximum = maxChoices ?: options.filterNot { it.disabled && !it.selected }.size
        binding.range.selectionStatus.setTextColor(colorActive)
        val textSizeSmall = resources.getDimensionPixelSize(R.dimen.textSizeBody)
        val textSpan =
            SpannableString(getString(R.string.current_of_total, selected, actualMaximum)).apply {
                setSpan(
                    AbsoluteSizeSpan(textSizeSmall), selected.toString().length, this.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        binding.range.selectionStatus.text = textSpan
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {

        savedState?.let { saved ->
            listener = saved.getSerializable(STATE_LISTENER) as OptionListener?
            listenerMultiple = saved.getSerializable(STATE_LISTENER_MULTIPLE) as OptionsListener?
            repeat(saved.getInt(STATE_OPTIONS_AMOUNT)) {
                options.add(saved.getSerializable(STATE_OPTIONS.plus(it)) as Option)
            }
            saved.getIntArray(STATE_OPTIONS_SELECTED)?.toMutableList()?.let {
                optionsSelected = it
            }
            mode = saved.getSerializable(STATE_MODE) as DisplayMode
            multipleChoices = saved.getBoolean(STATE_MULTIPLE_CHOICES)
            displayMultipleChoicesInfo = saved.getBoolean(STATE_MULTIPLE_CHOICES_INFO)
            maxChoicesStrict = saved.getBoolean(STATE_MAX_CHOICES_STRICT)
            displayButtons = saved.getBoolean(STATE_DISPLAY_BUTTONS)
            minChoices = saved.get(STATE_MIN_CHOICES) as Int?
            maxChoices = saved.get(STATE_MAX_CHOICES) as Int?
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            putSerializable(STATE_LISTENER, listener as Serializable?)
            putSerializable(STATE_LISTENER_MULTIPLE, listenerMultiple as Serializable?)
            putInt(STATE_OPTIONS_AMOUNT, options.size)
            options.forEachIndexed { i, option ->
                putSerializable(STATE_OPTIONS.plus(i), option)
            }
            putIntArray(STATE_OPTIONS_SELECTED, optionsSelected.toIntArray())
            putSerializable(STATE_MODE, mode)
            putBoolean(STATE_MULTIPLE_CHOICES, multipleChoices)
            putBoolean(STATE_MULTIPLE_CHOICES_INFO, displayMultipleChoicesInfo)
            putBoolean(STATE_MAX_CHOICES_STRICT, maxChoicesStrict)
            putBoolean(STATE_DISPLAY_BUTTONS, displayButtons)
            minChoices?.let { putInt(STATE_MIN_CHOICES, it) }
            maxChoices?.let { putInt(STATE_MAX_CHOICES, it) }
        }
    }

    /** Build [OptionsSheet] and show it later. */
    fun build(ctx: Context, viewWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT, func: OptionsSheet.() -> Unit): OptionsSheet {
        this.windowContext = ctx
        this.viewWidth = viewWidth
        this.func()
        return this
    }

    /** Build and show [OptionsSheet] directly. */
    fun show(ctx: Context, viewWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT, func: OptionsSheet.() -> Unit): OptionsSheet {
        this.windowContext = ctx
        this.viewWidth = viewWidth
        this.func()
        this.show()
        return this
    }
}
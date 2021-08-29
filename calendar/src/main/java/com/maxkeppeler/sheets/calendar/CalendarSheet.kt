/*
 * Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

@file:Suppress("unused")

package com.maxkeppeler.sheets.calendar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EdgeEffect
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.yearMonth
import com.maxkeppeler.sheets.calendar.databinding.SheetsCalendarBinding
import com.maxkeppeler.sheets.calendar.utils.toCalendar
import com.maxkeppeler.sheets.calendar.utils.toLocalDate
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.layoutmanagers.CustomGridLayoutManager
import com.maxkeppeler.sheets.core.layoutmanagers.CustomLinearLayoutManager
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.core.views.SheetsContent
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit

/** Listener which returns the selected date or selected start to end date. */
typealias CalendarDateListener = (dateStart: Calendar, dateEnd: Calendar?) -> Unit

/**
 * The [CalendarSheet] lets you pick a date or date range.
 */
class CalendarSheet : Sheet() {

    override val dialogTag = "CalendarSheet"

    private lateinit var binding: SheetsCalendarBinding
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthLayoutManger: CustomGridLayoutManager

    private lateinit var yearAdapter: YearAdapter
    private lateinit var yearLayoutManger: CustomLinearLayoutManager

    private val fullDate = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val dateRangeStartNoMonth = DateTimeFormatter.ofPattern("d")
    private val dateRangeStart = DateTimeFormatter.ofPattern("d MMM")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    private var colorTextActive: Int = 0
    private var colorText: Int = 0
    private var iconColor: Int = 0
    private var colorTextInverse: Int = 0
    private var highlightColor: Int = 0

    private lateinit var selectionShapeStart: InsetDrawable
    private lateinit var selectionShapeEnd: InsetDrawable

    private lateinit var selectionShapeStartBg: InsetDrawable
    private lateinit var selectionShapeEndBg: InsetDrawable

    private lateinit var selectionShapeEndLayer: LayerDrawable

    private lateinit var selectionShapeMiddle: InsetDrawable
    private lateinit var dayTodayDrawable: InsetDrawable
    private lateinit var disabledDayDrawable: InsetDrawable

    private var drawableAnimator: ValueAnimationListener? = null

    private var today = LocalDate.now()
    private var calendarViewActive: Boolean = true

    private var selectionMode: SelectionMode = SelectionMode.RANGE
    private var calendarMode: CalendarMode = CalendarMode.MONTH

    private var maxRange: Int = 7

    private var disabledDates: MutableList<LocalDate> = mutableListOf()
    private var listener: CalendarDateListener? = null

    private var disableTimeLine: TimeLine? = null

    private val disablePast: Boolean
        get() = disableTimeLine == TimeLine.PAST

    private val disableFuture: Boolean
        get() = disableTimeLine == TimeLine.FUTURE

    private var rangeYears: Int = 100
    private var selectedViewDate: LocalDate = LocalDate.now()
    private var selectedDate: LocalDate? = null
    private var selectedDateStart: LocalDate? = null
    private var selectedDateEnd: LocalDate? = null
    private var displayButtons = true

    /**
     * Set the date that should be marked as today. By default this is the current day.
     */
    fun setTodayDate(date: Calendar) {
        this.today = date.toLocalDate()
    }

    /**
     * Set the date that should be marked as today. By default this is the current day.
     */
    fun setTodayDate(date: LocalDate) {
        this.today = date
    }

    /**
     * Set the selected date.
     * @throws IllegalStateException if selection is not [SelectionMode.DATE]
     */
    fun setSelectedDate(date: Calendar) {
        if (this.selectionMode != SelectionMode.DATE) throw IllegalStateException("Only available for SelectionMode.DATE")
        else this.selectedDate = date.toLocalDate()
    }

    /**
     * Set the selected date.
     * @throws IllegalStateException if selection is not [SelectionMode.DATE]
     */
    fun setSelectedDate(date: LocalDate) {
        if (this.selectionMode != SelectionMode.DATE) throw IllegalStateException("Only available for SelectionMode.DATE")
        else this.selectedDate = date
    }

    /**
     * Set the selected date range.
     * @throws IllegalStateException if selection is not [SelectionMode.RANGE]
     */
    fun setSelectedDateRange(start: Calendar, end: Calendar) {
        if (this.selectionMode != SelectionMode.RANGE) {
            throw IllegalStateException("Only available for SelectionMode.RANGE")
        } else {
            this.selectedDateStart = start.toLocalDate()
            this.selectedDateEnd = end.toLocalDate()
        }
    }

    /**
     * Set the selected date range.
     * @throws IllegalStateException if selection is not [SelectionMode.RANGE]
     */
    fun setSelectedDateRange(start: LocalDate, end: LocalDate) {
        if (this.selectionMode != SelectionMode.RANGE) {
            throw IllegalStateException("Only available for SelectionMode.RANGE")
        } else {
            this.selectedDateStart = start
            this.selectedDateEnd = end
        }
    }

    /** Disable timeline into past or future. */
    fun disableTimeline(timeLine: TimeLine) {
        this.disableTimeLine = timeLine
    }

    /** Set the max duration of the date range. */
    fun maxRange(@IntRange(from = 2) maxRange: Int) {
        this.maxRange = maxRange
    }

    /** Set the [SelectionMode]. */
    fun selectionMode(selectionMode: SelectionMode) {
        this.selectionMode = selectionMode
    }

    /** Set the [CalendarMode]. */
    fun calendarMode(calendarMode: CalendarMode) {
        this.calendarMode = calendarMode
    }

    /** Set the range of years into the past and future. */
    fun rangeYears(@IntRange(from = 1, to = 200) rangeYears: Int) {
        this.rangeYears = rangeYears
    }

    /**
     * Add disabled dates which are not selectable as a date or within a range.
     *
     * @param disabledDates Instances of [Calendar].
     */
    fun disable(vararg disabledDates: Calendar) {
        this.disabledDates.addAll(disabledDates.mapTo(mutableListOf(), { it.toLocalDate() }))
    }

    /**
     * Add a disabled date which is not selectable as a date or within a range.
     *
     * @param disabledDate Instance of [Calendar].
     */
    fun disable(disabledDate: Calendar) {
        this.disabledDates.add(disabledDate.toLocalDate())
    }

    /**
     * Add disabled dates which are not selectable as a date or within a range.
     *
     * @param disabledDates Instances of [Calendar].
     */
    fun disable(vararg disabledDates: LocalDate) {
        this.disabledDates.addAll(disabledDates)
    }

    /**
     * Add a disabled date which is not selectable as a date or within a range.
     *
     * @param disabledDate Instance of [Calendar].
     */
    fun disable(disabledDate: LocalDate) {
        this.disabledDates.add(disabledDate)
    }

    /** Display buttons and require a positive button click. */
    fun displayButtons(displayButtons: Boolean = true) {
        this.displayButtons = displayButtons
    }

    /**
     * Set a listener.
     *
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveListener: CalendarDateListener) {
        this.listener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveRes The String resource id for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, positiveListener: CalendarDateListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.listener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveText The text for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveText: String, positiveListener: CalendarDateListener? = null) {
        this.positiveText = positiveText
        this.listener = positiveListener
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
        positiveListener: CalendarDateListener? = null,
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.listener = positiveListener
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
        positiveListener: CalendarDateListener? = null,
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.listener = positiveListener
    }

    override fun onCreateLayoutView(): View =
        SheetsCalendarBinding.inflate(LayoutInflater.from(activity))
            .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonPositiveListener(::save)
        displayButtonsView(displayButtons)
        validate(true)
        initResources()
        with(binding) {
            setupSwitchViews()
            setupMonths()
            setupYears()
            setupCalendarView()
        }
    }

    private fun initResources() {

        colorTextActive = getPrimaryColor(requireContext())
        iconColor = getIconColor(requireContext())
        highlightColor = getHighlightColor(requireContext())
        colorText = getTextColor(requireContext())
        colorTextInverse =
            getTextInverseColor(requireContext()).takeUnless { it.isColorDark() } ?: colorText

        val shapeModelRound = ShapeAppearanceModel().toBuilder().apply {
            setAllCorners(CornerFamily.ROUNDED, 45.getDp())
        }.build()

        /*
            Create InsetDrawables to create a padding around the
            actual drawable within the day item view.
         */

        selectionShapeStart = InsetDrawable(MaterialShapeDrawable(shapeModelRound).apply {
            fillColor = ColorStateList.valueOf(colorTextActive)
        }, 8.toDp(), 8.toDp(), 8.toDp(), 8.toDp())

        selectionShapeEnd = InsetDrawable(MaterialShapeDrawable(shapeModelRound).apply {
            fillColor = ColorStateList.valueOf(colorTextActive)
        }, 8.toDp(), 8.toDp(), 8.toDp(), 8.toDp())

        selectionShapeEndBg =
            InsetDrawable(MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply {
            }.build()).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            }, (-16).toDp(), 0.toDp(), 16.toDp(), 0.toDp())

        selectionShapeEndLayer =
            LayerDrawable(arrayOf(selectionShapeEnd, selectionShapeEndBg))

        selectionShapeStartBg =
            InsetDrawable(MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply {
            }.build()).apply {
                alpha = 90
                fillColor = ColorStateList.valueOf(highlightColor)
            }, 16.toDp(), 0.toDp(), (-8).toDp(), 0.toDp())

        selectionShapeMiddle = InsetDrawable(
            MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().build()).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            }, 0.toDp(), 8.toDp(), 0.toDp(), 8.toDp()
        )

        dayTodayDrawable = InsetDrawable(
            MaterialShapeDrawable(shapeModelRound).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            },
            8.toDp(),
            8.toDp(),
            8.toDp(),
            8.toDp()
        )

        binding.calendarView.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect =
                EdgeEffect(view.context).apply { color = getPrimaryColor(requireContext()) }
        }
    }

    /** Get the start selection drawable with a blend in background. */
    fun getSelectionShapeStartTransitionDrawable(bgAlpha: Float): LayerDrawable {
        selectionShapeStartBg.apply { alpha = bgAlpha.toInt() }
        return LayerDrawable(arrayOf(selectionShapeStart, selectionShapeStartBg))
    }

    private fun SheetsCalendarBinding.setupSwitchViews() {

        monthSpinner.buttonTintList = ColorStateList.valueOf(iconColor)
        yearSpinner.buttonTintList = ColorStateList.valueOf(iconColor)
        dateSelected.setTextColor(colorText)

        // With a click on the date, the user will always switch back to the normal calendar view
        dateSelected.setOnClickListener {
            if (calendarViewActive) return@setOnClickListener // Ignore if currently in calendar view
            yearSpinner.apply { if (isChecked) isChecked = false }
            monthSpinner.apply { if (isChecked) isChecked = false }
            switchToCalendarView()
        }

        monthContainer.setOnClickListener {
            monthSpinner.apply { isChecked = !isChecked; if (isChecked) switchToMonthsView() }
            yearSpinner.apply { if (isChecked) isChecked = false }
            if (!monthSpinner.isChecked && !yearSpinner.isChecked)
                switchToCalendarView()
        }

        yearContainer.setOnClickListener {
            yearSpinner.apply { isChecked = !isChecked; if (isChecked) switchToYearsView() }
            monthSpinner.apply { if (isChecked) isChecked = false }
            if (!monthSpinner.isChecked && !yearSpinner.isChecked)
                switchToCalendarView()
        }

    }

    private fun SheetsCalendarBinding.setupCalendarView() {

        when (selectionMode) {
            SelectionMode.DATE -> setCurrentDateText(selectedDate)
            SelectionMode.RANGE -> setCurrentDateRangeText(selectedDateStart, selectedDateEnd)
        }

        when (calendarMode) {

            CalendarMode.WEEK_1, CalendarMode.WEEK_2, CalendarMode.WEEK_3 ->
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    outDateStyle = OutDateStyle.END_OF_ROW,
                    maxRowCount = calendarMode.rows,
                    hasBoundaries = false
                )

            CalendarMode.MONTH ->
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    outDateStyle = OutDateStyle.END_OF_ROW,
                    maxRowCount = 6,
                    hasBoundaries = true
                )
        }

        val now: YearMonth = YearMonth.now()
        val start = if (disablePast) now else now.minusYears(rangeYears.toLong())
        val end = if (disableFuture) now else now.plusYears(rangeYears.toLong())

        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.scrollMode = ScrollMode.PAGED
        calendarView.setup(start, end, firstDayOfWeek)
        calendarView.scrollToDate(selectedDate ?: selectedDateStart ?: today)
        updateSpinnerValues()

        setupDayBinding()
        setupMonthHeaderBinding()

        setupMonthScrollListener()
    }

    private fun setupMonthScrollListener() {

        binding.calendarView.monthScrollListener = { month ->
            val day = month.weekDays.first().first { it.owner == DayOwner.THIS_MONTH }
            selectedViewDate = day.date
            monthAdapter.updateCurrentYearMonth(day.date.yearMonth)
            updateSpinnerValues()
        }
    }

    private fun setupDayBinding() {

        binding.calendarView.dayBinder = object : DayBinder<DayViewHolder> {
            override fun create(view: View) = DayViewHolder(view, ::onClickDay)
            override fun bind(container: DayViewHolder, day: CalendarDay) {
                container.day = day
                container.binding.day.text = day.day.toString()
                setupDayDesign(container, day)
            }
        }
    }

    private fun onClickDay(day: CalendarDay) {

        if (day.owner != DayOwner.THIS_MONTH) {
            // We don't want to
            return
        }

        when (selectionMode) {

            SelectionMode.DATE -> {
                if (!isDateDisabled(day)) {
                    selectedDate = day.date
                    selectedViewDate = day.date
                    setCurrentDateText(day.date)
                }
            }

            SelectionMode.RANGE -> {
                if (!isDateDisabled(day)) {
                    if (selectedDateStart != null) {
                        if (day.date < selectedDateStart || selectedDateEnd != null) {
                            selectedDateStart = day.date
                            selectedViewDate = day.date
                            selectedDateEnd = null
                        } else if (day.date != selectedDateStart) {
                            if (!containsSelectionDisabledDays(selectedDateStart!!, day.date)) {
                                selectedDateEnd = day.date
                                selectedViewDate = day.date
                            } else {
                                selectedDateStart = day.date
                                selectedViewDate = day.date
                            }
                        }
                    } else {
                        selectedDateStart = day.date
                        selectedViewDate = day.date
                    }
                    setCurrentDateRangeText(selectedDateStart, selectedDateEnd)
                }
            }
        }

        updateSpinnerValues()
        validate() // Validate selection
        binding.calendarView.notifyCalendarChanged() // Update calendar view
    }

    private fun setupDayDesign(container: DayViewHolder, day: CalendarDay) {

        // Hide days from another month
        if (day.owner != DayOwner.THIS_MONTH) {
            container.binding.shape.background = null
            container.binding.day.alpha = 0f
            return
        }

        // Disable day view
        if (isDateDisabled(day)) {
            container.binding.day.alpha = 0.25f
            container.binding.day.setTextColor(colorText)
            return
        }

        container.binding.day.alpha = 1f

        when {

            // Single date or range start day view
            selectedDate == day.date || selectedDateStart == day.date && selectedDateEnd == null -> {
                if (container.binding.shape.background != selectionShapeStart) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeStart
                    container.binding.shape.fadeIn()
                    container.binding.day.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    container.binding.day.setTextColor(colorTextInverse)
                }
            }

            // Range start with bg transition day view
            day.date == selectedDateStart -> {
                drawableAnimator = ValueAnimationListener(lifecycle, 0f, 255f, 300,
                    updateListener = {
                        container.binding.shape.background =
                            getSelectionShapeStartTransitionDrawable(it)
                    }, endListener = {
                        container.binding.shape.fadeIn()
                        container.binding.day.setTextAppearance(
                            requireContext(),
                            R.style.TextAppearance_MaterialComponents_Subtitle2
                        )
                        container.binding.day.setTextColor(colorTextInverse)
                    })
            }

            // Range middle day view
            selectedDateStart != null && selectedDateEnd != null && (day.date > selectedDateStart && day.date < selectedDateEnd) -> {
                if (container.binding.shape.background != selectionShapeMiddle) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeMiddle
                    container.binding.shape.fadeIn()
                }
            }

            // Range end day view
            day.date == selectedDateEnd -> {
                if (container.binding.shape.background != selectionShapeEndLayer) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeEndLayer
                    container.binding.shape.fadeIn()
                    container.binding.day.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    container.binding.day.setTextColor(colorTextInverse)
                }
            }

            // Today day view
            day.date == today -> {
                container.binding.shape.background = dayTodayDrawable
                container.binding.day.setTextAppearance(
                    requireContext(),
                    R.style.TextAppearance_MaterialComponents_Subtitle2
                )
                container.binding.day.setTextColor(colorTextActive)
            }

            // Normal day view
            else -> {
                container.binding.shape.background = null
                container.binding.day.setTextAppearance(
                    requireContext(),
                    R.style.TextAppearance_MaterialComponents_Body2
                )
                container.binding.day.setTextColor(colorText)
            }
        }
    }

    private fun containsSelectionDisabledDays(dateStart: LocalDate, dateEnd: LocalDate): Boolean =
        disabledDates.any { disabledDate ->

            val afterStart = dateStart.dayOfMonth <= disabledDate.dayOfMonth
                    && dateStart.year <= disabledDate.year
                    && dateStart.month.ordinal <= disabledDate.month.ordinal

            val afterEnd = dateEnd.dayOfMonth >= disabledDate.dayOfMonth
                    && dateEnd.year >= disabledDate.year
                    && dateEnd.month.ordinal >= disabledDate.month.ordinal

            afterStart && afterEnd
        }

    private fun isDateDisabled(day: CalendarDay): Boolean {
        return (disabledDates.any {
            day.date.dayOfMonth == it.dayOfMonth
                    && day.date.year == it.year
                    && day.date.month.ordinal == it.month.ordinal
        } || disablePast && day.date.isBefore(today) || disableFuture && day.date.isAfter(today))
    }

    private fun SheetsCalendarBinding.setupMonths() {

        monthsRecyclerView.setHasFixedSize(false)
        monthsRecyclerView.setItemViewCacheSize(12)

        monthAdapter = MonthAdapter(
            ctx = requireActivity(),
            disablePast = disablePast,
            disableFuture = disableFuture,
            listener = ::selectMonth
        ).apply {
            updateCurrentYearMonth(selectedViewDate.yearMonth)
        }
        monthsRecyclerView.adapter = monthAdapter

        val scrollable = calendarMode != CalendarMode.MONTH
        monthLayoutManger = CustomGridLayoutManager(requireContext(), 2, scrollable)
        monthsRecyclerView.layoutManager = monthLayoutManger

        monthsRecyclerView.post {

            if (calendarMode == CalendarMode.MONTH) {
                val height = monthsRecyclerView.measuredHeight.minus(32.getDp()).div(6).toInt()
                monthAdapter.updateItemHeight(height)
            }

            monthLayoutManger.scrollToPositionWithOffset(
                YearMonth.now().monthValue,
                monthsRecyclerView.height.div(2)
            )
        }
    }

    private fun SheetsCalendarBinding.setupYears() {

        val years = getYears()

        yearAdapter = YearAdapter(requireContext(), years, ::selectYear)
        yearsRecyclerView.adapter = yearAdapter

        yearLayoutManger = CustomLinearLayoutManager(requireContext(), true)
        yearsRecyclerView.layoutManager = yearLayoutManger
    }

    private fun setupMonthHeaderBinding() {

        val daysOfWeek = getDaysOfWeek()
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewHolder> {
            override fun create(view: View) = MonthViewHolder(view)
            override fun bind(holder: MonthViewHolder, month: CalendarMonth) {
                if (holder.legend.tag != null) return
                holder.legend.tag = month.yearMonth
                daysOfWeek.forEach { daysOfWeek ->
                    val textView = SheetsContent(requireContext())
                    textView.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    textView.text = daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    textView.layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                            weight = 1f
                        }
                    textView.setTextColor(colorText)
                    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textView.gravity = Gravity.CENTER
                    holder.legend.addView(textView)
                }
            }
        }
    }

    private fun switchToMonthsView() {
        calendarViewActive = false
        with(binding) {
            calendarView.visibility = View.INVISIBLE
            monthsRecyclerView.visibility = View.VISIBLE
            yearsRecyclerView.visibility = View.GONE
            if (calendarMode != CalendarMode.MONTH) {
                monthsRecyclerView.post {
                    monthLayoutManger.scrollToPositionWithOffset(
                        selectedViewDate.month.ordinal,
                        binding.monthsRecyclerView.height.div(2)
                    )
                }
            }
        }
    }

    private fun switchToYearsView() {
        calendarViewActive = false
        with(binding) {
            calendarView.visibility = View.INVISIBLE
            yearsRecyclerView.visibility = View.VISIBLE
            monthsRecyclerView.visibility = View.GONE
            val years = getYears()
            val index = years.indexOfFirst { it.value == selectedViewDate.year }
            yearsRecyclerView.post {
                yearLayoutManger.scrollToPositionWithOffset(
                    index,
                    binding.yearsRecyclerView.height.div(2)
                )
            }
        }
    }

    private fun switchToCalendarView() {
        calendarViewActive = true
        with(binding) {
            calendarView.visibility = View.VISIBLE
            yearsRecyclerView.visibility = View.GONE
            monthsRecyclerView.visibility = View.GONE
        }
    }

    private fun setCurrentDateText(date: LocalDate?) {
        with(binding) {
            dateSelected.text = date?.let { fullDate.format(date) } ?: "Select date"
        }
    }

    private fun setCurrentDateRangeText(dateStart: LocalDate?, dateEnd: LocalDate?) {
        with(binding) {
            val sameMonth = dateStart?.monthValue == dateEnd?.monthValue
            val rangeStartText = dateStart?.let {
                if (sameMonth) dateRangeStartNoMonth.format(it) else dateRangeStart.format(it)
            } ?: getString(R.string.sheets_date_range_from)
            val rangeEndText =
                dateEnd?.let { dateRangeStart.format(it) }
                    ?: getString(R.string.sheets_date_range_to)
            dateSelected.text = getString(R.string.sheets_date_range, rangeStartText, rangeEndText)
        }
    }

    private fun updateSpinnerValues() {
        updateMonthSpinner()
        updateYearSpinner()
    }

    private fun updateYearSpinner() {
        binding.valueSpinnerYear.text = yearFormatter.format(selectedViewDate)
    }

    private fun updateMonthSpinner() {
        binding.valueSpinnerMonth.text = monthFormatter.format(selectedViewDate)
    }

    private fun selectYear(year: Year) {
        binding.yearSpinner.isChecked = false
        selectedViewDate = LocalDate.of(year.value, selectedViewDate.month.value, 1)
        binding.calendarView.scrollToDate(selectedViewDate)
        monthAdapter.updateCurrentYearMonth(selectedViewDate.yearMonth)
        updateYearSpinner()
        switchToCalendarView()
    }

    private fun selectMonth(month: Month) {
        binding.monthSpinner.isChecked = false
        selectedViewDate = LocalDate.of(selectedViewDate.year, month.value, 1)
        binding.calendarView.scrollToDate(selectedViewDate)
        updateMonthSpinner()
        switchToCalendarView()
    }

    // From [https://github.com/kizitonwose/CalendarView/tree/master/sample/src/main/java/com/kizitonwose/calendarviewsample]
    private fun getDaysOfWeek(): Array<DayOfWeek> {
        val firstDay = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        if (firstDay != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDay.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDay.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    private fun getYears(): MutableList<Year> {

        val years = mutableListOf<Year>()

        val rangePast = if (!disablePast) -rangeYears..-1L else null
        val rangeFuture = if (!disableFuture) 1L..rangeYears else null

        val now = Year.now()
        rangePast?.forEach { years.add(now.plusYears(it)) }
        years.add(now)
        rangeFuture?.forEach { years.add(now.plusYears(it)) }

        return years
    }

    private fun validate(init: Boolean = false) {

        val maxRangeLengthDays = TimeUnit.DAYS.toSeconds(maxRange.toLong())

        val selectionInRange = selectedDateEnd != null && selectedDateEnd!!.atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC).minus(
                selectedDateStart!!.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
            ) < maxRangeLengthDays

        val selectionValid = selectionMode == SelectionMode.RANGE && selectionInRange
                || selectionMode == SelectionMode.DATE && selectedDate != null

        if (!displayButtons && selectionValid) {
            Handler(Looper.getMainLooper()).postDelayed({
                save()
            }, 600)
        } else displayButtonPositive(selectionValid, !init)
    }

    private fun save() {

        val selectedDateCalendar = selectedDate?.toCalendar()
        val selectedDateRangeStartCalendar = selectedDateStart?.toCalendar()
        val selectedDateRangeEndCalendar = selectedDateEnd?.toCalendar()

        listener?.invoke(
            selectedDateCalendar ?: selectedDateRangeStartCalendar!!,
            selectedDateRangeEndCalendar
        )

        drawableAnimator?.cancel()
        dismiss()
    }

    /** Build [CalendarSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: CalendarSheet.() -> Unit): CalendarSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [CalendarSheet] directly. */
    fun show(ctx: Context, width: Int? = null, func: CalendarSheet.() -> Unit): CalendarSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }
}
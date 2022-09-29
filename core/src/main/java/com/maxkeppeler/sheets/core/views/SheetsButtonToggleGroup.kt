@file:Suppress("unused", "PrivatePropertyName")

package com.maxkeppeler.sheets.core.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.sheets.core.R
import com.maxkeppeler.sheets.core.utils.*

/** Listener that returns the index of the selected button. */
typealias ButtonToggleGroupSelectionListener = (Int) -> Unit

/** Custom ButtonToggleGroup implementation with auto-scroll ability.*/
class SheetButtonToggleGroup
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.horizontalScrollViewStyle,
) : HorizontalScrollView(ctx, attrs, defStyleAttr) {

    private var listener: ButtonToggleGroupSelectionListener? = null
    private var index: Int = 0
    private var selectedIndex: Int? = null
    private var options: List<String> = listOf()
    private var buttons = mutableListOf<MaterialButton>()

    private var group: MaterialButtonToggleGroup = MaterialButtonToggleGroup(ctx).apply {
        isSingleSelection = false
        clipToPadding = false
        addOnButtonCheckedListener { group, checkedId, isChecked ->
            val button = findViewById<MaterialButton>(checkedId)
            val index = group.indexOfChild(button)
            select(index, isChecked)
        }
    }

    private val primaryColor by lazy { getPrimaryColor(context) }
    private val highlightColor by lazy { getHighlightColor(ctx) }
    private val buttonTextBaseColor by lazy { colorOfAttr(ctx, R.attr.colorOnSurface) }
    private val backgroundColor by lazy { colorOfAttr(ctx, android.R.attr.colorBackground) }
    private val strokeBaseColor by lazy { colorOfAttr(ctx, R.attr.colorOnSurface) }

    private val TEXT_STATES =
        arrayOf(intArrayOf(android.R.attr.state_enabled,
            android.R.attr.state_checkable,
            android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled,
                android.R.attr.state_checkable,
                -android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf())

    private val TEXT_COLORS = intArrayOf(primaryColor,
        buttonTextBaseColor.withAlpha(0.60f),
        primaryColor,
        buttonTextBaseColor.withAlpha(0.38f))

    private val STROKE_STATES =
        arrayOf(intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked))

    private val STROKE_COLORS = intArrayOf(primaryColor, strokeBaseColor.withAlpha(0.12f))

    private val RIPPLE_STATES =
        arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused, android.R.attr.state_hovered),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_hovered), intArrayOf(0))

    private val rippleColors = intArrayOf(highlightColor,
        highlightColor,
        highlightColor,
        highlightColor,
        Color.TRANSPARENT)

    val BG_STATES =
        arrayOf(intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked))

    val bgColors = intArrayOf(highlightColor, Color.TRANSPARENT)


    /** Set the selection listener. */
    fun listener(listener: ButtonToggleGroupSelectionListener) {
        this.listener = listener
    }

    /** Select a specific index. */
    fun selected(index: Int) {
        selectedIndex = index
    }

    /** Set the button options. */
    fun options(options: List<String>) {
        this.options = options
        createButtons()
    }

    init {
        clipToPadding = false
        isHorizontalScrollBarEnabled = false
        addView(group)
    }

    private fun createButtons() {

        options.forEachIndexed { i, value ->
            val btn = MaterialButton(context, null, R.attr.materialButtonOutlinedStyle).apply {
                text = value
                tag = value.plus(i)
                minWidth = 0
                minimumWidth = 16.toDp()
                shapeAppearanceModel = ShapeAppearanceModel().toBuilder().apply {
                    setAllCorners(CornerFamily.ROUNDED, 8.getDp())
                }.build()
                setTextColor(ColorStateList(TEXT_STATES, TEXT_COLORS))
                strokeColor = ColorStateList(STROKE_STATES, STROKE_COLORS)
                rippleColor = ColorStateList(RIPPLE_STATES, rippleColors)
                backgroundTintList = ColorStateList(BG_STATES, bgColors)
            }
            buttons.add(btn)
            group.addView(btn)
        }
        addAutoScrollIfNecessary()
        selectedIndex?.let { buttons[it].isChecked = true }
    }

    private fun addAutoScrollIfNecessary() {
        post {
            if (group.width > width) group.addOnButtonCheckedListener(scrollListener)
            else group.removeOnButtonCheckedListener(scrollListener)
        }
    }

    private fun select(i: Int, isChecked: Boolean) {
        if (index == i || !isChecked) {
            buttons[index].isChecked = true
        } else {
            val prevIndex = index
            index = i
            listener?.invoke(i)
            buttons[prevIndex].isChecked = false
        }
    }

    private val scrollListener =
        MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, _ ->

            val button = findViewById<MaterialButton>(checkedId)
            val index = group.indexOfChild(button)

            val listUntilStartButton = buttons.subList(0, index)
            val buttonWidth = (listUntilStartButton.lastOrNull()?.width ?: 0)

            val widthButtons = listUntilStartButton.sumBy { it.width }
            val start = widthButtons - buttonWidth.times(1.5)
            smoothScrollTo(start.toInt(), 0)
        }
}

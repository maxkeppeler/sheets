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
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.core.views.SheetsContent
import com.maxkeppeler.sheets.options.databinding.SheetsOptionsGridItemBinding
import com.maxkeppeler.sheets.options.databinding.SheetsOptionsListItemBinding

internal class OptionsAdapter(
    private val ctx: Context,
    private val options: MutableList<Option>,
    private val globalPreventIconTint: Boolean?,
    private val type: DisplayMode,
    private val multipleChoice: Boolean,
    private val collapsedItems: Boolean,
    private val listener: OptionsSelectionListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG_DISABLED_SELECTED = "tag_disabled_selected"
        private const val SELECTOR_STATE_DISABLED_INDEX = 0
        private const val SELECTOR_STATE_SELECTED_INDEX = 1
    }

    private val selectedOptions =
        mutableMapOf<Int, Triple<ImageView, SheetsContent, SheetsContent>>()

    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)
    private val highlightColor = getHighlightColor(ctx)

    private val selectedTextColor =
        colorOfAttr(ctx, R.attr.sheetsOptionSelectedTextColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    private val selectedIconsColor =
        colorOfAttr(ctx, R.attr.sheetsOptionSelectedImageColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    private val disabledTextColor =
        colorOfAttr(ctx, R.attr.sheetsOptionDisabledTextColor).takeUnlessNotResolved()
            ?: getTextColor(ctx)

    private val disabledIconsColor =
        colorOfAttr(ctx, R.attr.sheetsOptionDisabledImageColor).takeUnlessNotResolved()
            ?: getIconColor(ctx)

    private val disabledBackgroundColor =
        colorOfAttr(ctx, R.attr.sheetsOptionDisabledBackgroundColor).takeUnlessNotResolved()
            ?: colorOf(ctx, R.color.sheetsOptionDisabledColor)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (type) {
            DisplayMode.GRID_HORIZONTAL, DisplayMode.GRID_VERTICAL -> {
                GridItem(
                    SheetsOptionsGridItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            DisplayMode.LIST -> {
                ListItem(
                    SheetsOptionsListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is GridItem -> holder.binding.buildGridItem(i)
            is ListItem -> holder.binding.buildListItem(i)
        }
    }

    private fun SheetsOptionsListItemBinding.buildListItem(i: Int) {

        val option = options[i]

        title.text = option.titleTextRes?.let { ctx.getString(it) } ?: option.titleText ?: ""

        val subtitleText = option.subtitleTextRes?.let { ctx.getString(it) } ?: option.subtitleText
        subtitle.visibility = if (subtitleText != null) View.VISIBLE else View.GONE
        subtitle.text = subtitleText

        option.drawable?.let {
            icon.setImageDrawable(it)
            icon.visibility = View.VISIBLE
        }

        option.drawableRes?.let { res ->
            icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
            icon.visibility = View.VISIBLE
        }

        optionContainer.setOnLongClickListener { option.longClickListener?.invoke(); true }
        optionContainer.changeRippleAndStateColor()

        val selected = option.selected || listener.isSelected(i)

        if (option.disabled && !selected) {
            showDisabled(title, subtitle, icon, optionContainer)
        } else {

            if (option.disabled && selected) optionContainer.tag = TAG_DISABLED_SELECTED
            else optionContainer.setOnClickListener {
                selectOption(i, title, subtitle, icon, optionContainer)
            }

            if (selected) selectOption(i, title, subtitle, icon, optionContainer)
            else showDeselected(i, title, subtitle, icon, optionContainer)
        }
    }

    private fun SheetsOptionsGridItemBinding.buildGridItem(i: Int) {

        val option = options[i]

        if (collapsedItems) {
            root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        title.text = option.titleTextRes?.let { ctx.getString(it) } ?: option.titleText ?: ""

        val subtitleText = option.subtitleTextRes?.let { ctx.getString(it) } ?: option.subtitleText
        subtitle.visibility = if (subtitleText != null) View.VISIBLE else View.GONE
        subtitle.text = subtitleText

        option.drawable?.let {
            icon.setImageDrawable(it)
            icon.visibility = View.VISIBLE
        }

        option.drawableRes?.let { res ->
            icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
            icon.visibility = View.VISIBLE
        }

        optionContainer.setOnLongClickListener { option.longClickListener?.invoke(); true }
        optionContainer.changeRippleAndStateColor()

        val selected = option.selected || listener.isSelected(i)

        if (option.disabled && !selected) {
            showDisabled(title, subtitle, icon, optionContainer)
        } else {

            if (option.disabled && selected) optionContainer.tag = TAG_DISABLED_SELECTED
            else optionContainer.setOnClickListener {
                selectOption(i, title, subtitle, icon, optionContainer)
            }

            if (selected) selectOption(i, title, subtitle, icon, optionContainer)
            else showDeselected(i, title, subtitle, icon, optionContainer)
        }
    }

    private fun showDisabled(
        title: SheetsContent,
        subtitle: SheetsContent,
        icon: ImageView,
        root: View,
    ) {
        title.setTextColor(disabledTextColor)
        subtitle.setTextColor(disabledTextColor)
        icon.setColorFilter(disabledIconsColor)
        root.changeRippleAndStateColor(
            Color.TRANSPARENT,
            SELECTOR_STATE_DISABLED_INDEX,
            disabledBackgroundColor
        )
        root.isActivated = true
    }

    private fun showSelected(
        title: SheetsContent,
        subtitle: SheetsContent,
        icon: ImageView,
        root: View,
    ) {

        title.setTextColor(selectedTextColor)
        subtitle.setTextColor(selectedTextColor)
        icon.setColorFilter(selectedIconsColor)

        if (root.tag == TAG_DISABLED_SELECTED) {
            root.changeRippleAndStateColor(Color.TRANSPARENT)
        }

        if (multipleChoice) {
            root.isSelected = true
        }
    }

    private fun showDeselected(
        index: Int,
        title: SheetsContent,
        subtitle: SheetsContent,
        icon: ImageView,
        root: View,
    ) {
        val option = options[index]
        with(option) {

            val titleColor =
                defaultTitleColor ?: defaultTitleColorRes?.let { ContextCompat.getColor(ctx, it) }
                ?: textColor

            val subtitleColor = defaultSubtitleColor ?: defaultSubtitleColorRes?.let {
                ContextCompat.getColor(ctx,
                    it)
            } ?: textColor

            val preventIconTint = option.preventIconTint ?: globalPreventIconTint
            val iconsColor = defaultIconColor
                ?: defaultIconColorRes?.let { ContextCompat.getColor(ctx, it) }
                ?: takeUnless { preventIconTint == true }?.let { iconsColor }

            title.setTextColor(titleColor)
            subtitle.setTextColor(subtitleColor)
            iconsColor?.let { color ->
                icon.setColorFilter(color)
            } ?: run {
                icon.clearColorFilter()
            }
        }
        if (multipleChoice) {
            root.isSelected = false
        }
    }

    private fun View.changeRippleAndStateColor(
        rippleColor: Int = highlightColor,
        stateIndex: Int = SELECTOR_STATE_SELECTED_INDEX,
        stateBackgroundColor: Int = highlightColor,
    ) {
        // Ripple drawable
        (background as RippleDrawable).apply {
            setColor(ColorStateList.valueOf(rippleColor))
            // Selector drawable
            (getDrawable(1) as StateListDrawable).apply {
                // Selected state drawable
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    (getStateDrawable(stateIndex) as GradientDrawable).apply {
                        color = ColorStateList.valueOf(stateBackgroundColor)
                    }
                }
            }
        }
    }

    private fun selectOption(
        index: Int,
        title: SheetsContent,
        subtitle: SheetsContent,
        icon: ImageView,
        root: View,
    ) {
        if (multipleChoice) {
            if (!listener.isMultipleChoiceSelectionAllowed(index)) return
            if (selectedOptions.contains(index)) {
                listener.deselectMultipleChoice(index)
                selectedOptions[index]?.let {
                    showDeselected(index,
                        it.second,
                        it.third,
                        it.first,
                        root)
                }
                selectedOptions.remove(index)
            } else {
                listener.selectMultipleChoice(index)
                selectedOptions[index] = Triple(icon, title, subtitle)
                showSelected(title, subtitle, icon, root)
            }
        } else {
            selectedOptions.forEach { option ->
                showDeselected(option.key,
                    option.value.second,
                    option.value.third,
                    option.value.first,
                    root)
            }
            selectedOptions.clear()
            selectedOptions[index] = Triple(icon, title, subtitle)
            showSelected(title, subtitle, icon, root)
            listener.select(index)
        }
    }

    override fun getItemCount(): Int = options.size

    inner class GridItem(val binding: SheetsOptionsGridItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ListItem(val binding: SheetsOptionsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
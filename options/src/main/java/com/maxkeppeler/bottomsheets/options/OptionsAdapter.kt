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

package com.maxkeppeler.bottomsheets.options

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.util.StateSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.ripple.RippleDrawableCompat
import com.google.android.material.ripple.RippleUtils
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.maxkeppeler.bottomsheets.core.utils.*
import com.maxkeppeler.bottomsheets.core.views.BottomSheetContent
import com.maxkeppeler.bottomsheets.options.databinding.BottomSheetsOptionsGridItemBinding
import com.maxkeppeler.bottomsheets.options.databinding.BottomSheetsOptionsListItemBinding

internal class OptionsAdapter(
    private val ctx: Context,
    private val options: MutableList<Option>,
    private val type: DisplayMode,
    private val multipleChoice: Boolean,
    private val collapsedItems: Boolean,
    private val listener: OptionsSelectionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedOptions = mutableMapOf<Int, Pair<ImageView, BottomSheetContent>>()

    private val selectedTextColor =
        colorOfAttr(ctx, R.attr.bottomSheetOptionActiveTextColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)
    private val selectedImageColor =
        colorOfAttr(ctx, R.attr.bottomSheetOptionActiveImageColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)
    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)
    private val highlightColor = getHighlightColor(ctx)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (type) {
            DisplayMode.GRID_HORIZONTAL, DisplayMode.GRID_VERTICAL -> {
                GridItem(
                    BottomSheetsOptionsGridItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            DisplayMode.LIST -> {
                ListItem(
                    BottomSheetsOptionsListItemBinding.inflate(
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

    private fun BottomSheetsOptionsListItemBinding.buildListItem(i: Int) {

        val option = options[i]
        val text = option.textRes?.let { ctx.getString(it) } ?: option.text ?: ""

        optionContainer.applyColorToDrawable()

        option.drawableRes?.let { res ->
            icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
            icon.visibility = View.VISIBLE
        }

        label.text = text

        if (option.disabled) {
            if (!option.selected) {
                optionContainer.isActivated = true
            }
        } else {
            optionContainer.isActivated = false
            optionContainer.setOnClickListener {
                selectOption(i, label, icon, optionContainer)
            }
        }

        if (option.selected) {
            selectOption(i, label, icon, optionContainer)
        } else {
            showDeselected(label, icon, optionContainer)
        }
    }

    private fun BottomSheetsOptionsGridItemBinding.buildGridItem(i: Int) {

        val option = options[i]
        val text = option.textRes?.let { ctx.getString(it) } ?: option.text ?: ""

        optionContainer.applyColorToDrawable()

        if (collapsedItems) {
            root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        option.drawableRes?.let { res ->
            icon.setImageDrawable(ContextCompat.getDrawable(ctx, res))
            icon.visibility = View.VISIBLE
        }

        label.text = text

        if (option.disabled) {
            if (!option.selected) {
                optionContainer.isActivated = true
            }
        } else {
            optionContainer.isActivated = false
            optionContainer.setOnClickListener {
                selectOption(i, label, icon, optionContainer)
            }
        }

        if (option.selected) {
            selectOption(i, label, icon, optionContainer)
        } else {
            showDeselected(label, icon, optionContainer)
        }
    }

    private fun showSelected(label: BottomSheetContent, icon: ImageView, root: View) {
        label.setTextColor(selectedTextColor)
        icon.setColorFilter(selectedImageColor)
        if (multipleChoice) {
            root.isSelected = true
        }
    }

    private fun showDeselected(label: BottomSheetContent, icon: ImageView, root: View) {
        label.setTextColor(textColor)
        icon.setColorFilter(iconsColor)
        if (multipleChoice) {
            root.isSelected = false
        }
    }

    private fun View.applyColorToDrawable() {
        // Ripple drawable
        (background as RippleDrawable).apply {
            setColor(ColorStateList.valueOf(highlightColor))
            // Selector drawable
            (getDrawable(1) as StateListDrawable).apply {
                // Selected state drawable
                (getStateDrawable(1) as GradientDrawable).apply {
                    color = ColorStateList.valueOf(highlightColor)
                }
            }
        }
    }

    private fun selectOption(index: Int, label: BottomSheetContent, icon: ImageView, root: View) {
        if (multipleChoice) {
            if (!listener.isMultipleChoiceSelectionAllowed(index)) return
            if (selectedOptions.contains(index)) {
                listener.deselectMultipleChoice(index)
                selectedOptions[index]?.let { showDeselected(it.second, it.first, root) }
                selectedOptions.remove(index)
            } else {
                listener.selectMultipleChoice(index)
                selectedOptions[index] = Pair(icon, label)
                showSelected(label, icon, root)
            }
        } else {
            selectedOptions.forEach { showDeselected(it.value.second, it.value.first, root) }
            selectedOptions.clear()
            selectedOptions[index] = Pair(icon, label)
            showSelected(label, icon, root)
            listener.select(index)
        }
    }

    override fun getItemCount(): Int = options.size

    inner class GridItem(val binding: BottomSheetsOptionsGridItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ListItem(val binding: BottomSheetsOptionsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
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

package com.maxkeppeler.sheets.color

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.color.databinding.SheetsColorTemplatesItemBinding

internal class ColorAdapter(
    @ColorInt
    private val colors: MutableList<Int>,
    @ColorInt
    private var selectedColor: Int,
    private val callback: ColorListener
) : RecyclerView.Adapter<ColorAdapter.ColorItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorItem =
        ColorItem(
            SheetsColorTemplatesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ColorItem, i: Int) {
        val color = colors[i]
        with(holder.binding) {

            val background = colorView.background as RippleDrawable
            (background.getDrawable(1) as GradientDrawable).setColor(color)

            root.setOnClickListener {
                select(color)
                callback.invoke(color)
            }

            if (color == selectedColor)
                colorActive.visibility = View.VISIBLE
            else
                colorActive.visibility = View.GONE
        }
    }

    private fun select(color: Int) {
        notifyItemChanged(colors.indexOf(selectedColor))
        notifyItemChanged(colors.indexOf(color))
        selectedColor = color
    }

    fun removeSelection() {
        notifyItemChanged(colors.indexOf(selectedColor))
        selectedColor = RecyclerView.NO_POSITION
    }

    override fun getItemCount(): Int = colors.size

    inner class ColorItem(val binding: SheetsColorTemplatesItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
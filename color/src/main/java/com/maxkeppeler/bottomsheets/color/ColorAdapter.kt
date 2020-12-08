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

package com.maxkeppeler.bottomsheets.color

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.bottomsheets.color.databinding.BottomSheetsColorTemplatesItemBinding
import com.maxkeppeler.bottomsheets.core.utils.colorOf

class ColorAdapter(
    private val ctx: Context,
    private val colors: MutableList<Int>,
    private val callback: ColorListener
) : RecyclerView.Adapter<ColorAdapter.ColorItem>() {

    private var selectedItem: ImageView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorItem =
        ColorItem(
            BottomSheetsColorTemplatesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ColorItem, i: Int) {
        val color = colorOf(ctx, colors[i])
        with(holder.binding) {
            val background = colorView.background as RippleDrawable
            (background.getDrawable(1) as GradientDrawable).setColor(color)
            root.setOnClickListener {
                select(colorActive)
                callback.invoke(color)
            }
        }
    }

    private fun select(image: ImageView) {
        selectedItem?.visibility = View.GONE
        selectedItem = image
        selectedItem?.visibility = View.VISIBLE
    }

    fun removeSelection() {
        selectedItem?.visibility = View.GONE
        selectedItem = null
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = colors.size

    inner class ColorItem(val binding: BottomSheetsColorTemplatesItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
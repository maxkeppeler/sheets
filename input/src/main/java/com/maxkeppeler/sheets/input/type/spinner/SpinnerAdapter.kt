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

package com.maxkeppeler.sheets.input.type.spinner

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.maxkeppeler.sheets.input.databinding.SheetsSpinnerItemBinding

internal open class SpinnerAdapter(
    private val ctx: Context,
    private var options: List<SpinnerOption> = listOf(),
) : BaseAdapter() {

    override fun getCount(): Int = options.size

    override fun getItem(position: Int): SpinnerOption = options[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int, convertView: View?,
        parent: ViewGroup,
    ): View = createViewFromResource(position,
        convertView,
        parent)

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {

        val binding = convertView?.let { SheetsSpinnerItemBinding.bind(it) }
            ?: SheetsSpinnerItemBinding.inflate(
                LayoutInflater.from(ctx), parent, false)

        val option = options[position]
        val text = option.text ?: option.textRes?.let { ctx.getString(it) }
        val drawable = option.drawable ?: option.drawableRes?.let { ContextCompat.getDrawable(ctx, it) }

        binding.text.text = text
        binding.icon.setImageDrawable(drawable)
        option.drawableTintRes?.let {
            binding.icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx, it))
        }
        binding.icon.visibility = if (drawable != null) View.VISIBLE else View.GONE

        return binding.root
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup,
    ): View = createViewFromResource(
        position,
        convertView,
        parent
    )
}
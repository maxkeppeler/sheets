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

package com.maxkeppeler.sheets.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.calendar.databinding.SheetsCalendarYearItemBinding
import com.maxkeppeler.sheets.core.utils.getPrimaryColor
import com.maxkeppeler.sheets.core.utils.getTextColor
import java.time.Year
import java.time.format.DateTimeFormatter

/** Listener that is invoked if a year is selected. */
internal typealias YearSelectedListener = (Year) -> Unit

internal class YearAdapter(
    private val ctx: Context,
    private val years: MutableList<Year>,
    private val listener: YearSelectedListener
) : RecyclerView.Adapter<YearAdapter.YearItem>() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy")
    private var selectedYear = Year.now()
    private var currentYear = Year.now()

    private val primaryColor = getPrimaryColor(ctx)
    private val textColor = getTextColor(ctx)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearItem =
        YearItem(
            SheetsCalendarYearItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: YearItem, i: Int) {
        val year = years[i]
        holder.binding.year.text = formatter.format(year.atDay(1))
        holder.binding.handleState(year)
        holder.binding.root.setOnClickListener {
            listener.invoke(year)
            updateSelectedYear(year)
        }
    }

    private fun SheetsCalendarYearItemBinding.handleState(yearAtIndex: Year) = when {
        currentYear == yearAtIndex && selectedYear == yearAtIndex -> {
            year.isSelected = true
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            year.setTextColor(primaryColor)
        }
        currentYear == yearAtIndex -> {
            year.isSelected = true
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            year.setTextColor(primaryColor)
        }
        selectedYear == yearAtIndex -> {
            year.isSelected = false
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            year.setTextColor(primaryColor)
        }
        else -> {
            year.isSelected = false
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            year.setTextColor(textColor)
        }
    }

    private fun updateSelectedYear(year: Year) {
        selectedYear = year
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = years.size

    inner class YearItem(val binding: SheetsCalendarYearItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
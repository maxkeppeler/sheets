package com.maxkeppeler.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maxkeppeler.sample.databinding.LayoutHeaderItemBinding
import com.maxkeppeler.sample.databinding.LayoutSheetItemBinding
import com.maxkeppeler.sample.utils.SheetExample
import com.maxkeppeler.sample.utils.SheetType

internal typealias ExampleClickListener = (type: SheetExample) -> Any

internal class BottomSheetExampleAdapter(
    private val ctx: Context,
    private val listener: ExampleClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val examples = mutableListOf<Any>().apply {
        SheetExample.values().groupBy { it.type }.forEach {
            add(it.key)
            addAll(it.value)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (examples[position] is SheetType) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderItem(
                    LayoutHeaderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
            else -> {
                ExampleItem(
                    LayoutSheetItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is HeaderItem -> holder.binding.buildHeader(i)
            is ExampleItem -> holder.binding.buildExample(i)
        }
    }

    private fun LayoutHeaderItemBinding.buildHeader(i: Int) {
        (root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
            isFullSpan = true
        }
        val type = examples[i] as SheetType
        headerTitle.text = ctx.getString(type.titleRes)
        headerDesc.text = ctx.getString(type.descRes)
    }

    private fun LayoutSheetItemBinding.buildExample(i: Int) {
        val example = examples[i] as SheetExample
        val indexOfType = examples.filter { it is SheetExample && example.type == it.type }.indexOf(example).plus(1)
        title.text = "$indexOfType. Sample"
        content.text = ctx.getString(example.textRes)
        root.setOnClickListener { listener.invoke(example) }
    }

    override fun getItemCount(): Int = examples.size

    inner class HeaderItem(val binding: LayoutHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ExampleItem(val binding: LayoutSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
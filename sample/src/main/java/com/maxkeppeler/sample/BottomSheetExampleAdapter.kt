package com.maxkeppeler.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maxkeppeler.sample.databinding.LayoutHeaderBottomSheetBinding
import com.maxkeppeler.sample.databinding.LayoutItemBottomSheetBinding
import com.maxkeppeler.sample.utils.BottomSheetExample
import com.maxkeppeler.sample.utils.BottomSheetType

internal typealias ExampleClickListener = (type: BottomSheetExample) -> Any

internal class BottomSheetExampleAdapter(
    private val ctx: Context,
    private val listener: ExampleClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val examples = mutableListOf<Any>().apply {
        BottomSheetExample.values().groupBy { it.type }.forEach {
            add(it.key)
            addAll(it.value)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (examples[position] is BottomSheetType) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderItem(
                    LayoutHeaderBottomSheetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
            else -> {
                ExampleItem(
                    LayoutItemBottomSheetBinding.inflate(
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

    private fun LayoutHeaderBottomSheetBinding.buildHeader(i: Int) {
        (root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
            isFullSpan = true
        }
        val type = examples[i] as BottomSheetType
        headerTitle.text = ctx.getString(type.titleRes)
    }

    private fun LayoutItemBottomSheetBinding.buildExample(i: Int) {
        val example = examples[i]  as BottomSheetExample
        title.text = ctx.getString(example.textRes)
        root.setOnClickListener { listener.invoke(example) }
    }

    override fun getItemCount(): Int = examples.size

    inner class HeaderItem(val binding: LayoutHeaderBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ExampleItem(val binding: LayoutItemBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root)

}
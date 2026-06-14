package com.example.expense.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.data.model.ByCategory
import com.example.expense.databinding.ItemLegendRowBinding

class LegendAdapter(private val list: List<ByCategory>) : RecyclerView.Adapter<LegendAdapter.LegendViewHolder>() {

    inner class LegendViewHolder(val binding: ItemLegendRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {
        val binding = ItemLegendRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LegendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int) {
        val item = list[position]

        holder.binding.apply {
            // Set color

            viewColor.background.setTint(Color.parseColor(item.categoryColor))

            // Set text
            tvLegend.text = "${item.categoryName} ${item.percentage}%"
        }
    }

    override fun getItemCount() = list.size
}
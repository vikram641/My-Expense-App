package com.example.expense.ui.adapter

import com.example.expense.R
import android.content.res.ColorStateList
import com.example.expense.core.base.BaseAdapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.expense.data.model.ByCategory
import com.example.expense.databinding.ItemAnalyticsBinding

class AnalyticsCategoryAdapter: BaseAdapter<ByCategory , ItemAnalyticsBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAnalyticsBinding {
        return ItemAnalyticsBinding.inflate(inflater,parent , false)
    }

    override fun bind(
        binding: ItemAnalyticsBinding,
        item: ByCategory,
        position: Int
    ) {
        binding.tvName.text = item.categoryName
        binding.tvAmount.text = "₹${item.amount}"
        binding.progress.progress = item.percentage
        binding.progress.apply {
            progress = item.percentage
            progressTintList = ColorStateList.valueOf(Color.parseColor(item.categoryColor))
        }

        // Real per-category icon instead of a hardcoded 🍔 for every row - same
        // getIdentifier + fallback pattern BudgetAdapter uses.
        val context = binding.root.context
        val resId = context.resources.getIdentifier(item.categoryIcon, "drawable", context.packageName)
        binding.icon.setImageResource(if (resId != 0) resId else R.drawable.ic_transport)

        val color = try {
            Color.parseColor(item.categoryColor)
        } catch (e: Exception) {
            Color.GRAY
        }
        binding.icon.backgroundTintList = ColorStateList.valueOf(color)
    }

}
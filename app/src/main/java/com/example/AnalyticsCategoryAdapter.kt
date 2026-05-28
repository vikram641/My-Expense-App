package com.example

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.data.model.ByCategory
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


    }

}
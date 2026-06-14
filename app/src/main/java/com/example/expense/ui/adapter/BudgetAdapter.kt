package com.example.expense.ui.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import com.example.expense.core.base.BaseAdapter
import com.example.expense.core.util.Utils
import com.example.expense.data.model.BudgetData
import com.example.expense.databinding.ItemBudgetBinding
import javax.inject.Inject

class BudgetAdapter (private val utils: Utils):
    BaseAdapter<BudgetData, ItemBudgetBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemBudgetBinding.inflate(inflater, parent, false)

    override fun bind(
        binding: ItemBudgetBinding,
        item: BudgetData,
        position: Int
    ) {
        binding.tvCategory.text = item.categoryName
        binding.tvAmount.text = "₹${item.spentAmount}/₹${item.limitAmount}"

        val percentage = utils.calculatePercentage(item.spentAmount,item.limitAmount)

// 🎨 set color from API (hex like "#4ECDC4")
        val color = try {
            val hex = if (item.categoryColor.startsWith("#"))
                item.categoryColor
            else
                "#${item.categoryColor}"

            android.graphics.Color.parseColor(hex)

        } catch (e: Exception) {
            android.graphics.Color.GRAY
        }

// ✅ apply tint BEFORE animation
        binding.progress.progressTintList =
            android.content.res.ColorStateList.valueOf(color)

// 🎯 animate progress
        val animator = ObjectAnimator.ofInt(
            binding.progress,
            "progress",
            0,
            percentage
        )

        animator.duration = 800
        animator.start()
    }
}
package com.example.expense.feature.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.local.CategoryEntity
import com.example.expense.databinding.ItemCategoryBinding

/**
 * Multi-select category picker for SetupBudgetFragment - reuses the same item_category.xml
 * row (icon + name) and bg_category_selected/normal drawables as ExpenseCatAdapter, but
 * that adapter is single-select only, so this is a separate small adapter rather than
 * bending ExpenseCatAdapter's selectedPosition model to support a Set.
 */
class OnboardingCategoryAdapter(
    private val onToggle: (CategoryEntity) -> Unit
) : ListAdapter<CategoryEntity, OnboardingCategoryAdapter.ViewHolder>(DIFF) {

    private val selectedIds = mutableSetOf<String>()

    fun setSelectedIds(ids: Set<String>) {
        selectedIds.clear()
        selectedIds.addAll(ids)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryEntity) {
            binding.tvCategoryName.text = category.name
            val iconResId = binding.root.context.resources.getIdentifier(
                category.icon, "drawable", binding.root.context.packageName
            )
            if (iconResId != 0) {
                binding.tvCategoryIcon.setImageResource(iconResId)
            }
            binding.tvCategoryIcon.setBackgroundResource(
                if (selectedIds.contains(category.id)) R.drawable.bg_category_selected
                else R.drawable.bg_category_normal
            )
            binding.root.setOnClickListener { onToggle(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CategoryEntity>() {
            override fun areItemsTheSame(old: CategoryEntity, new: CategoryEntity) = old.id == new.id
            override fun areContentsTheSame(old: CategoryEntity, new: CategoryEntity) = old == new
        }
    }
}

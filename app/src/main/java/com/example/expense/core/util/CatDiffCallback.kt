package com.example.expense.core.util

import androidx.recyclerview.widget.DiffUtil
import com.example.expense.data.model.CatDataResponse

class CatDiffCallback : DiffUtil.ItemCallback<CatDataResponse>() {
    override fun areItemsTheSame(oldItem: CatDataResponse, newItem: CatDataResponse): Boolean {
        return oldItem.id == newItem.id  // compare unique IDs
    }

    override fun areContentsTheSame(oldItem: CatDataResponse, newItem: CatDataResponse): Boolean {
        return oldItem == newItem  // full equality check
    }
}
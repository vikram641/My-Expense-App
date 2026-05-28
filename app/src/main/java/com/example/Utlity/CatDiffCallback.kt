package com.example.Utlity

import androidx.recyclerview.widget.DiffUtil
import com.example.data.model.CatDataResponse

class CatDiffCallback : DiffUtil.ItemCallback<CatDataResponse>() {
    override fun areItemsTheSame(oldItem: CatDataResponse, newItem: CatDataResponse): Boolean {
        return oldItem.id == newItem.id  // compare unique IDs
    }

    override fun areContentsTheSame(oldItem: CatDataResponse, newItem: CatDataResponse): Boolean {
        return oldItem == newItem  // full equality check
    }
}
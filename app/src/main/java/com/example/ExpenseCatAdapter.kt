package com.example

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup


import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Utlity.CatDiffCallback
import com.example.data.model.CatDataResponse
import com.example.expense.R
import com.example.expense.databinding.ItemCategoryBinding


class ExpenseCatAdapter(
    private val onItemClick: ((CatDataResponse) -> Unit)? = null
) : ListAdapter<CatDataResponse, ExpenseCatAdapter.ExpenseViewHolder>(CatDiffCallback()) {
    private var selectedPosition = -1

    inner class ExpenseViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(catDataResponse: CatDataResponse){
            binding.tvCategoryName.text = catDataResponse.name
            val iconResId = binding.root.context.resources.getIdentifier(
                catDataResponse.icon,   // correct
                "drawable",
                binding.root.context.packageName
            )

            if (iconResId != 0) {
                binding.tvCategoryIcon.setImageResource(iconResId)
            } else {
//                binding.tvCategoryIcon.setImageResource(R.drawable.)
            }

            binding.root.setOnClickListener {
                val previous = selectedPosition
                val current = adapterPosition

                if (current == RecyclerView.NO_POSITION) return@setOnClickListener

                selectedPosition = current

                notifyItemChanged(previous)
                notifyItemChanged(selectedPosition)
                onItemClick?.invoke(catDataResponse) }



        }


        }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ExpenseViewHolder {

        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        holder.bind(getItem(position))

        val isSelected = position == selectedPosition


        holder.binding.tvCategoryIcon.setBackgroundResource(
            if (isSelected)
                R.drawable.bg_category_selected
            else
                R.drawable.bg_category_normal
        )


    }

//    override fun getItemCount(): Int {
//       return list.size
//
//    }



}
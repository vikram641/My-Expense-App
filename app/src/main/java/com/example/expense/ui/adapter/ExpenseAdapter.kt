package com.example.expense.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.model.ExpenseX
import com.example.expense.ui.model.ExpenseListItem
import com.example.expense.databinding.ItemDateHeaderBinding
import com.example.expense.databinding.ItemExpensesBinding

class ExpenseAdapter : ListAdapter<ExpenseListItem, RecyclerView.ViewHolder>(DIFF) {

    var onItemClick: ((ExpenseX) -> Unit)? = null


    companion object {
        const val HEADER  = 0
        const val EXPENSE = 1

        val DIFF = object : DiffUtil.ItemCallback<ExpenseListItem>() {

            override fun areItemsTheSame(
                oldItem: ExpenseListItem,
                newItem: ExpenseListItem
            ): Boolean {
                return when {
                    oldItem is ExpenseListItem.Header &&
                            newItem is ExpenseListItem.Header ->
                        oldItem.title == newItem.title

                    oldItem is ExpenseListItem.ExpenseItem &&
                            newItem is ExpenseListItem.ExpenseItem ->
                        oldItem.expense.id == newItem.expense.id

                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: ExpenseListItem,
                newItem: ExpenseListItem
            ) = oldItem == newItem
        }
    }

    inner class ExpenseViewHolder(
        val binding: ItemExpensesBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHolder(
        val binding: ItemDateHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ExpenseListItem.Header      -> HEADER
            is ExpenseListItem.ExpenseItem -> EXPENSE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            HeaderViewHolder(
                ItemDateHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            ExpenseViewHolder(
                ItemExpensesBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (val item = getItem(position)) {

            is ExpenseListItem.Header -> {
                (holder as HeaderViewHolder).binding.tvHeader.text = item.title
            }

            is ExpenseListItem.ExpenseItem -> {
                val expense = item.expense
                val b       = (holder as ExpenseViewHolder).binding
                val ctx     = holder.itemView.context

                // Icon
                val resId = ctx.resources.getIdentifier(
                    expense.category.icon, "drawable", ctx.packageName
                )
                b.imgLogo.setImageResource(
                    if (resId != 0) resId else R.drawable.ic_transport
                )

                // Color tint
                val color = try {
                    val hex = if (expense.category.color.startsWith("#"))
                        expense.category.color
                    else
                        "#${expense.category.color}"
                    Color.parseColor(hex)
                } catch (e: Exception) {
                    Color.GRAY
                }
                b.imgLogo.background?.setTint(color)

                // Data
                b.tvNote.text  = expense.note
                b.date.text    = expense.date
                b.amount.text  = "-₹${expense.amount}"

                holder.itemView.setOnClickListener { onItemClick?.invoke(expense) }
            }
        }
    }
}
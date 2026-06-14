package com.example.expense.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding> :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>>() {

    protected val items = mutableListOf<T>()

    // 🔥 click listener
    var onItemClick: ((T, Int) -> Unit)? = null

    class BaseViewHolder<VB : ViewBinding>(
        val binding: VB
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<VB> {

        val binding = inflateBinding(
            LayoutInflater.from(parent.context),
            parent
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<VB>,
        position: Int
    ) {
        val item = items[position]

        bind(holder.binding, item, position)

        // 👇 click handle here (centralized)
        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(item, position)
        }
    }

    override fun getItemCount() = items.size

    abstract fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VB

    abstract fun bind(
        binding: VB,
        item: T,
        position: Int
    )

    fun submitList(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
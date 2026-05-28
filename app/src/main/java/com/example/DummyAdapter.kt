package com.example

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DummyAdapter(private val count: Int) :
    RecyclerView.Adapter<DummyAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val tv = TextView(parent.context).apply {
            setPadding(40, 60, 40, 60)
            textSize = 16f
        }
        return VH(tv)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        (holder.itemView as TextView).text = "Item $position"
    }

    override fun getItemCount() = count
}
package com.example.expense.feature.chat

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R

private const val TYPE_USER = 0
private const val TYPE_ASSISTANT = 1
private const val FALLBACK_BAR_COLOR = "#7B61FF"

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ChatMessage>()

    fun submitList(newItems: List<ChatMessage>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is ChatMessage.User -> TYPE_USER
        is ChatMessage.Assistant -> TYPE_ASSISTANT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            UserViewHolder(inflater.inflate(R.layout.item_chat_user, parent, false))
        } else {
            AssistantViewHolder(inflater.inflate(R.layout.item_chat_assistant, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ChatMessage.User -> (holder as UserViewHolder).bind(item)
            is ChatMessage.Assistant -> (holder as AssistantViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    private class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        fun bind(message: ChatMessage.User) {
            tvMessage.text = message.text
        }
    }

    private class AssistantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        private val categoryContainer: LinearLayout = view.findViewById(R.id.categoryContainer)

        fun bind(message: ChatMessage.Assistant) {
            tvMessage.text = if (message.isLoading) "Thinking…" else message.text
            tvMessage.alpha = if (message.isLoading) 0.6f else 1f

            categoryContainer.removeAllViews()
            if (message.categories.isEmpty()) {
                categoryContainer.visibility = View.GONE
                return
            }
            categoryContainer.visibility = View.VISIBLE

            val maxAmount = message.categories.maxOf { it.amount }.coerceAtLeast(1)
            val inflater = LayoutInflater.from(categoryContainer.context)
            val density = categoryContainer.resources.displayMetrics.density

            message.categories.forEach { cat ->
                val row = inflater.inflate(R.layout.item_chat_category_bar, categoryContainer, false)
                row.findViewById<TextView>(R.id.tvCatName).text = cat.name
                row.findViewById<TextView>(R.id.tvCatAmount).text = cat.amount.toString()

                val barFill = row.findViewById<View>(R.id.barFill)
                val barSpacer = row.findViewById<View>(R.id.barSpacer)
                val fraction = (cat.amount.toFloat() / maxAmount).coerceIn(0.06f, 1f)
                (barFill.layoutParams as LinearLayout.LayoutParams).weight = fraction
                (barSpacer.layoutParams as LinearLayout.LayoutParams).weight = 1f - fraction

                val color = try {
                    Color.parseColor(cat.color)
                } catch (e: IllegalArgumentException) {
                    Color.parseColor(FALLBACK_BAR_COLOR)
                }
                barFill.background = GradientDrawable().apply {
                    cornerRadius = 4f * density
                    setColor(color)
                }

                categoryContainer.addView(row)
            }
        }
    }
}

package com.example.expense.feature.chat

/** A category/amount pair with its real app color resolved locally - Gemini
 * only ever returns the name and amount, never a color it would have to guess. */
data class CategoryBar(val name: String, val amount: Int, val color: String)

sealed class ChatMessage {
    data class User(val text: String) : ChatMessage()

    data class Assistant(
        val text: String,
        val categories: List<CategoryBar> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false
    ) : ChatMessage()
}

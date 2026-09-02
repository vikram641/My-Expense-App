package com.example.expense.core.util

import android.graphics.Color

/**
 * Deterministic vivid background color per avatar emoji, shared between the avatar
 * picker's badges and every screen that displays the chosen avatar (Home, Settings,
 * Edit Profile) so the two always match instead of the picker showing color but the
 * actual avatar circle staying a single fixed accent regardless of which one is picked.
 */
object AvatarPalette {

    private val colors = listOf(
        "#7C6FDD", "#4CAF82", "#F5A623", "#E74C3C",
        "#3FA7D6", "#FF6F91", "#00BFA6", "#B565D8"
    )

    fun colorFor(emoji: String): Int {
        val index = (emoji.hashCode() and Int.MAX_VALUE) % colors.size
        return Color.parseColor(colors[index])
    }
}

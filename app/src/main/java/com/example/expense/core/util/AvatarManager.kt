package com.example.expense.core.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE)

    fun saveAvatar(emoji: String) = prefs.edit().putString("avatar", emoji).apply()
    fun getAvatar(): String? = prefs.getString("avatar", null)
    fun clearAvatar() = prefs.edit().remove("avatar").apply()
}

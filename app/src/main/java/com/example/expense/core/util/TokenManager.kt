package com.example.expense.core.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("prefs_token_file", Context.MODE_PRIVATE)

    fun saveToken(token: String) = prefs.edit().putString("user_token", token).apply()
    fun getToken(): String? = prefs.getString("user_token", null)

    fun saveRefreshToken(token: String) = prefs.edit().putString("refresh_token", token).apply()
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun clearSession() = prefs.edit().clear().apply()
}

package com.example.Utlity

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences("prefs_token_file", Context.MODE_PRIVATE)

    fun saveToken(token:String){
        val editor = prefs.edit()
        editor.putString("user_token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString("user_token", null)
    }
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
package com.example.expense.core.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.auth0.android.jwt.JWT
@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("prefs_token_file", Context.MODE_PRIVATE)

    fun saveToken(token: String) = prefs.edit().putString("user_token", token).apply()
    fun getToken(): String? = prefs.getString("user_token", null)

    fun getUserId(): String? {
        // JWT(...) throws (not returns null) when handed a string that isn't actually a
        // token - which is exactly what an empty/missing token looks like since login is
        // bypassed in offline mode (see CLAUDE.md). Without this guard, every local-only
        // write that reads a userId (e.g. Repository.addExpenseLocal) throws instead of
        // just falling back to "", breaking that write with no token ever in play.
        val token = getToken()
        if (token.isNullOrEmpty()) return null
        return try {
            JWT(token).getClaim("id").asString()
        } catch (e: Exception) {
            null
        }
    }

    fun saveRefreshToken(token: String) = prefs.edit().putString("refresh_token", token).apply()
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    /** Last FCM registration token that was successfully sent to the backend, used to dedupe re-sends. */
    fun saveFcmToken(token: String) = prefs.edit().putString("fcm_token", token).apply()
    fun getFcmToken(): String? = prefs.getString("fcm_token", null)

    fun clearSession() = prefs.edit().clear().apply()
}

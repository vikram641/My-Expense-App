package com.example.expense.core.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tracks which GET endpoints have already been fetched in the current app session.
 * startNewSession() is called once per process start (in ExpenseApp.onCreate) so
 * all flags reset and screens refetch fresh data on next app launch.
 *
 * JSON data for simple objects (summary, profile) is stored alongside the flags so
 * the app can serve cached data even if a ViewModel is recreated mid-session.
 */
@Singleton
class SessionCacheManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("prefs_session_cache", Context.MODE_PRIVATE)

    fun startNewSession() = prefs.edit().clear().apply()

    fun isFetched(key: String): Boolean = prefs.getBoolean("f_$key", false)

    fun markFetched(key: String) = prefs.edit().putBoolean("f_$key", true).apply()

    fun invalidate(key: String) =
        prefs.edit().remove("f_$key").remove("d_$key").apply()

    fun putString(key: String, json: String) {
        prefs.edit().putString("d_$key", json).apply()
        markFetched(key)
    }

    fun getString(key: String): String? = prefs.getString("d_$key", null)

    companion object {
        const val KEY_HOME_SUMMARY = "home_summary"
        const val KEY_WEEKLY_SUMMARY = "weekly_summary"
        const val KEY_USER_PROFILE = "user_profile"
        const val KEY_CATEGORIES = "categories"

        fun homeSummaryKey(month: String?) = "${KEY_HOME_SUMMARY}_${month ?: "current"}"
        fun budgetKey(month: String?) = "budgets_${month ?: "all"}"
    }
}

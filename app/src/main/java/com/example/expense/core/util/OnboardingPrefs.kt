package com.example.expense.core.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local-only first-run profile (name / monthly budget / currency) collected by the
 * onboarding flow. The app currently runs fully offline (see CLAUDE.md "Offline mode"
 * section) so this is not yet synced anywhere - it's purely what gates whether
 * MainActivity starts its nav graph at welcomeFragment or dashboardFragment.
 */
@Singleton
class OnboardingPrefs @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("prefs_onboarding", Context.MODE_PRIVATE)

    fun isOnboardingComplete(): Boolean = prefs.getBoolean("onboarding_complete", false)

    fun saveProfile(name: String, monthlyBudget: Int, currencyCode: String) {
        prefs.edit()
            .putString("user_name", name)
            .putInt("monthly_budget", monthlyBudget)
            .putString("currency_code", currencyCode)
            .putBoolean("onboarding_complete", true)
            .apply()
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getMonthlyBudget(): Int = prefs.getInt("monthly_budget", 0)
    fun getCurrencyCode(): String = prefs.getString("currency_code", "INR") ?: "INR"
}

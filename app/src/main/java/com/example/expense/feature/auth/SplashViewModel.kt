package com.example.expense.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.NavEvent
import com.example.expense.core.util.OnboardingPrefs
import com.example.expense.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val onboardingPrefs: OnboardingPrefs
) : ViewModel() {

    private val _splashState = MutableStateFlow<NavEvent>(NavEvent.LOADING)
    val splashState: StateFlow<NavEvent> = _splashState

    fun verifyUserToken() {
        viewModelScope.launch {
            if (!onboardingPrefs.isOnboardingComplete()) {
                _splashState.value = NavEvent.ONBOARDING
                return@launch
            }

            // Login is temporarily bypassed while the app runs fully offline - see
            // CLAUDE.md "Offline mode (temporary)". Onboarding now replaces it as the
            // only gate before Home; re-enable this check when login comes back.
            // val token = tokenManager.getToken()
            // _splashState.value = if (!token.isNullOrEmpty()) NavEvent.HOME else NavEvent.LOGIN
            _splashState.value = NavEvent.HOME
        }
    }
}

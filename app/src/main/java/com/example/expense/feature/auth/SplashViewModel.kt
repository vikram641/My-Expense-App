package com.example.expense.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.NavEvent
import com.example.expense.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _splashState = MutableStateFlow<NavEvent>(NavEvent.LOADING)
    val splashState: StateFlow<NavEvent> = _splashState

    fun verifyUserToken() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            _splashState.value = if (!token.isNullOrEmpty()) NavEvent.HOME else NavEvent.LOGIN
        }
    }
}

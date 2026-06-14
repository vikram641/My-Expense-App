package com.example.expense.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.UiState
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.ChangePasswordRequest
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.UpdateProfileRequest
import com.example.expense.data.model.User
import com.example.expense.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _settingState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)
    val settingState: StateFlow<UiState<ApiResponse<User>>> = _settingState

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val logoutState: StateFlow<UiState<Unit>> = _logoutState

    private val _updateProfileState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)
    val updateProfileState: StateFlow<UiState<ApiResponse<User>>> = _updateProfileState

    private val _changePasswordState = MutableStateFlow<UiState<ApiResponse<DeleteResponse>>>(UiState.Idle)
    val changePasswordState: StateFlow<UiState<ApiResponse<DeleteResponse>>> = _changePasswordState

    fun getUserProfile() {
        viewModelScope.launch {
            _settingState.value = repository.getUserProfileDetail()
        }
    }

    fun logout(refreshToken: String) {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading
            _logoutState.value = repository.logout(refreshToken)
        }
    }

    fun updateProfile(name: String, currency: String) {
        viewModelScope.launch {
            _updateProfileState.value = UiState.Loading
            _updateProfileState.value = repository.updateUserProfile(UpdateProfileRequest(name = name, currency = currency))
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = UiState.Loading
            _changePasswordState.value = repository.changePassword(
                ChangePasswordRequest(currentPassword, newPassword)
            )
        }
    }
}
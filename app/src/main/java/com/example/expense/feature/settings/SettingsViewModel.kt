package com.example.expense.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.UiState
import com.example.expense.core.util.ExpenseCsvExporter
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.ChangePasswordRequest
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.UpdateProfileRequest
import com.example.expense.data.model.User
import com.example.expense.data.repository.Repository
import com.example.expense.ui.dialog.ExportRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: Repository,
    private val csvExporter: ExpenseCsvExporter
) : ViewModel() {

    private val _settingState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)
    val settingState: StateFlow<UiState<ApiResponse<User>>> = _settingState

    private val _exportState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val exportState: StateFlow<UiState<String>> = _exportState

    fun exportData(range: ExportRange) {
        viewModelScope.launch {
            _exportState.value = UiState.Loading
            val expenses = when (range) {
                is ExportRange.Monthly -> repository.getExpensesForExportMonth(range.month)
                is ExportRange.Custom -> repository.getExpensesForExportRange(range.fromDate, range.toDate)
            }
            val fileName = when (range) {
                is ExportRange.Monthly -> "expenses_${range.month}.csv"
                is ExportRange.Custom -> "expenses_${range.fromDate}_to_${range.toDate}.csv"
            }
            _exportState.value = csvExporter.export(expenses, fileName)
        }
    }

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

    /**
     * The syncData() push-to-server step is disabled while the app runs fully offline - see
     * CLAUDE.md "Offline mode (temporary)". This currently behaves the same as
     * [logoutWithoutSync]; restore the commented block to bring the pre-logout sync back.
     */
    fun logout(refreshToken: String) {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading

            repository.ClearAllLocalData()
            _logoutState.value = repository.logout(refreshToken)

//            when (val syncResult = repository.syncData()) {
//
//                is UiState.Success -> {
//                    // Sync completed successfully
//                    repository.ClearAllLocalData()
//
//                    // Call logout API
//                    _logoutState.value = repository.logout(refreshToken)
//                }
//
//                is UiState.Error -> {
//                    _logoutState.value = UiState.Error(syncResult.message)
//                }
//
//                is UiState.Loading -> {
//                    _logoutState.value = UiState.Loading
//                }
//
//                is UiState.Idle -> {
//                    _logoutState.value = UiState.Idle
//                }
//            }
        }
    }
    fun logoutWithoutSync(refreshToken: String) {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading
            repository.ClearAllLocalData()
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
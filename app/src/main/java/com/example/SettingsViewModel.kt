package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.model.ApiResponse
import com.example.data.model.Repository
import com.example.data.UiState
import com.example.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _settingState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)

    val settingState = _settingState

    fun getUserProfile(){
        viewModelScope.launch {
            _settingState.value = repository.getUserProfileDetail()
        }
    }
}
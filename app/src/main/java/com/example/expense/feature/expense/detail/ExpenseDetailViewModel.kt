package com.example.expense.feature.expense.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.UiState
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _detailState = MutableStateFlow<UiState<ApiResponse<ExpenseX>>>(UiState.Idle)
    val detailState: StateFlow<UiState<ApiResponse<ExpenseX>>> = _detailState

    private val _deleteState = MutableStateFlow<UiState<ApiResponse<DeleteResponse>>>(UiState.Idle)
    val deleteState: StateFlow<UiState<ApiResponse<DeleteResponse>>> = _deleteState

    fun getExpenseDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            _detailState.value = repository.getExpenseDetail(id)
        }
    }

    fun deleteExpense(id: String) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            _deleteState.value = repository.deleteExpense(id)
        }
    }
}

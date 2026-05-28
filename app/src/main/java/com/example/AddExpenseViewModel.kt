package com.example

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AddExpenseRequest
import com.example.data.model.AddExpenseResponse
import com.example.data.model.ApiResponse
import com.example.data.model.CatDataResponse
import com.example.data.model.Repository
import com.example.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel@Inject constructor(private val repository: Repository) : ViewModel() {
    private val _categoryState = MutableStateFlow<UiState<ApiResponse<List<CatDataResponse>>>>(UiState.Idle)
    val categoryState = _categoryState

    private val _addExpenseState = MutableStateFlow<UiState<ApiResponse<AddExpenseResponse>>>(UiState.Idle)
    val addExpenseState = _addExpenseState



    fun getExpenseCat(){
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            val response = repository.getExpenseCat()
            _categoryState.value = response
        }
    }

    fun addExpense(addExpenseRequest: AddExpenseRequest){
        viewModelScope.launch {
            val response = repository.addExpense(addExpenseRequest)
            _addExpenseState.value = response

        }
    }










}
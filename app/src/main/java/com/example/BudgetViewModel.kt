package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ApiResponse
import com.example.data.model.BudgetData
import com.example.data.model.CatDataResponse
import com.example.data.model.Repository
import com.example.data.model.SetBudgetRequest
import com.example.data.UiState
import com.example.data.model.setBudgetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _budgetState = MutableStateFlow<UiState<ApiResponse<List<BudgetData>>>>(UiState.Idle)
    val budgetState = _budgetState
    private val _categoryState = MutableStateFlow<UiState<ApiResponse<List<CatDataResponse>>>>(UiState.Idle)
    val categoryState = _categoryState

    private val _setBudgetState = MutableStateFlow<UiState<ApiResponse<setBudgetResponse>>>(UiState.Idle)
    val setBudgetState = _setBudgetState



    fun getBudgets(month: String?){
        viewModelScope.launch {
            _budgetState.value = repository.getBudgets(month)
        }
    }

    fun getExpenseCat(){
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            val response = repository.getExpenseCat()
            _categoryState.value = response
        }
    }

    fun setBudget(setBudgetRequest: SetBudgetRequest){
        viewModelScope.launch {
            _setBudgetState.value = repository.setBudget(setBudgetRequest)
        }
    }

    





}
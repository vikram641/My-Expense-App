package com.example

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ApiResponse
import com.example.data.model.ExpenseQuery
import com.example.data.model.ExpenseX
import com.example.data.model.ExpensesResponse
import com.example.data.model.Repository
import com.example.data.Api.SearchApiData
import com.example.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _expenseState = MutableStateFlow<UiState<ApiResponse<SearchApiData>>>(UiState.Idle)
    val expenseState  = _expenseState


    fun getExpensesSearch(expenseQuery: ExpenseQuery){
        viewModelScope.launch {
            _expenseState.value = repository.getExpensesSearch(expenseQuery)
            Log.d("vvv",repository.getExpensesSearch(expenseQuery).toString())

        }

    }

}
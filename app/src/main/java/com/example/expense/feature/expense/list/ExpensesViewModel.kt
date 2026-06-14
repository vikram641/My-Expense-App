package com.example.expense.feature.expense.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.ExpenseQuery
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.ExpensesResponse
import com.example.expense.data.repository.Repository

import com.example.expense.data.model.SearchApiData
import com.example.expense.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _expenseState = MutableStateFlow<UiState<ApiResponse<SearchApiData>>>(UiState.Idle)
    val expenseState = _expenseState

    // ---- Offline-first cache ----

    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow<String?>(null)

    private val cachedExpenses: StateFlow<List<ExpenseX>> =
        repository.observeCachedExpenses()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Filtered view of the local cache — always available offline. */
    val displayExpenses: StateFlow<List<ExpenseX>> = combine(
        cachedExpenses,
        _selectedCategory,
        _searchQuery
    ) { expenses, category, search ->
        expenses
            .filter { e -> category == null || e.category.name.equals(category, ignoreCase = true) }
            .filter { e -> search.isNullOrEmpty() || e.note.contains(search, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Network state of the most recent refresh (cache stays visible on error). */
    private val _refreshState = MutableStateFlow<UiState<ApiResponse<SearchApiData>>>(UiState.Idle)
    val refreshState: StateFlow<UiState<ApiResponse<SearchApiData>>> = _refreshState

    fun setCategory(category: String?) { _selectedCategory.value = category }
    fun setSearch(query: String?) { _searchQuery.value = query }

    /** Offline-first refresh: pulls from API and writes into Room cache. */
    fun refresh(expenseQuery: ExpenseQuery = ExpenseQuery(page = 1, limit = 50)) {
        viewModelScope.launch {
            _refreshState.value = UiState.Loading
            _refreshState.value = repository.refreshExpenses(expenseQuery)
        }
    }

    fun getExpensesSearch(expenseQuery: ExpenseQuery) {
        viewModelScope.launch {
            _expenseState.value = repository.getExpensesSearch(expenseQuery)
        }
    }
}

package com.example.expense.feature.expense.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // ---- Local-only cache ----

    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow<String?>(null)

    private val cachedExpenses: StateFlow<List<ExpenseX>> =
        repository.observeCachedExpenses()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Filtered view of the local cache. */
    val displayExpenses: StateFlow<List<ExpenseX>> = combine(
        cachedExpenses,
        _selectedCategory,
        _searchQuery
    ) { expenses, category, search ->
        expenses
            .filter { e -> category == null || e.category.name.equals(category, ignoreCase = true) }
            .filter { e -> search.isNullOrEmpty() || e.note.contains(search, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCategory(category: String?) { _selectedCategory.value = category }
    fun setSearch(query: String?) { _searchQuery.value = query }
}

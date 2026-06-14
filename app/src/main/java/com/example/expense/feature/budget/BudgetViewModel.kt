package com.example.expense.feature.budget

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.local.OperationResult
import com.example.expense.data.local.SyncResult
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.SetBudgetRequest
import com.example.expense.data.repository.Repository
import com.example.expense.core.UiState
import com.example.expense.sync.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _budgetState = MutableStateFlow<UiState<ApiResponse<List<BudgetData>>>>(UiState.Idle)
    val budgetState = _budgetState

    private val _categoryState = MutableStateFlow<UiState<ApiResponse<List<CatDataResponse>>>>(UiState.Idle)
    val categoryState = _categoryState

    private val _setBudgetResult = MutableStateFlow<OperationResult?>(null)
    val setBudgetResult: StateFlow<OperationResult?> = _setBudgetResult

    private val _syncResult = MutableStateFlow<SyncResult?>(null)
    val syncResult: StateFlow<SyncResult?> = _syncResult

    /** Live count of budgets waiting to sync — wire to a badge. */
    val pendingBudgetCount: StateFlow<Int> =
        repository.observePendingBudgetCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /**
     * Fetches budgets for [month], serving from Room cache when already fetched
     * this session. Pass forceRefresh = true on pull-to-refresh.
     */
    fun getBudgets(month: String?, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _budgetState.value = UiState.Loading
            _budgetState.value = repository.getBudgets(month, forceRefresh)
        }
    }

    /**
     * Fetches categories once per session (cached in Room after first call).
     */
    fun getExpenseCat(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            _categoryState.value = repository.getExpenseCat(forceRefresh)
        }
    }

    /**
     * Offline-first SET budget:
     * - Online  → posts to server immediately, invalidates cache.
     * - Offline → queues locally; SyncWorker pushes it when connectivity returns.
     */
    fun setBudget(request: SetBudgetRequest) {
        viewModelScope.launch {
            _setBudgetResult.value = repository.setBudgetOfflineFirst(request)
            // Trigger an immediate sync attempt in case we're online
            SyncWorker.enqueueOneTime(context)
        }
    }

    fun syncPending() {
        viewModelScope.launch {
            _syncResult.value = repository.syncPendingBudgets()
        }
    }
}

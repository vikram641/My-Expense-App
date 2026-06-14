package com.example.expense.feature.expense.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.local.AddExpenseResult
import com.example.expense.data.local.SyncResult
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.CatDataResponse
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
class AddExpenseViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _categoryState = MutableStateFlow<UiState<ApiResponse<List<CatDataResponse>>>>(UiState.Idle)
    val categoryState = _categoryState

    private val _addExpenseState = MutableStateFlow<UiState<ApiResponse<AddExpenseResponse>>>(UiState.Idle)
    val addExpenseState = _addExpenseState

    private val _addResult = MutableStateFlow<AddExpenseResult?>(null)
    val addResult: StateFlow<AddExpenseResult?> = _addResult

    private val _syncResult = MutableStateFlow<SyncResult?>(null)
    val syncResult: StateFlow<SyncResult?> = _syncResult

    /** Live count of expenses waiting to sync — wire this to a badge. */
    val pendingCount: StateFlow<Int> =
        repository.observePendingExpenseCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /**
     * Fetches categories once per session (served from Room on repeat visits).
     */
    fun getExpenseCat(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            _categoryState.value = repository.getExpenseCat(forceRefresh)
        }
    }

    /** Original online-only add. Kept for backward compatibility. */
    fun addExpense(addExpenseRequest: AddExpenseRequest) {
        viewModelScope.launch {
            val response = repository.addExpense(addExpenseRequest)
            _addExpenseState.value = response
        }
    }

    /**
     * Offline-first add: queues locally when offline, posts immediately when online.
     * After adding, enqueues a one-time WorkManager sync so queued items are
     * uploaded as soon as the device is back online.
     */
    fun addExpenseOfflineFirst(addExpenseRequest: AddExpenseRequest) {
        viewModelScope.launch {
            _addResult.value = repository.addExpenseOfflineFirst(addExpenseRequest)
            SyncWorker.enqueueOneTime(context)
        }
    }

    /** Manually drain the pending queue. No-ops when offline. Safe to call anytime. */
    fun syncPending() {
        viewModelScope.launch {
            _syncResult.value = repository.syncPendingExpenses()
        }
    }
}

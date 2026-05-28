package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ExpenseSummaryResponse
import com.example.data.model.Repository
import com.example.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _analyticsSate = MutableStateFlow<UiState<ExpenseSummaryResponse>>(UiState.Idle)
    val analyticsSate = _analyticsSate


    fun getSummary(month: String?){
        viewModelScope.launch {
            _analyticsSate.value = repository.getSummary(month)

        }
    }





}
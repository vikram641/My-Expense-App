package com.example.expense.feature.home

import android.widget.ProgressBar
import android.animation.ObjectAnimator
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.network.NetworkMonitor
import com.example.expense.core.util.Utils
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.ExpenseSummaryResponse
import com.example.expense.data.repository.Repository
import com.example.expense.core.UiState
import com.example.expense.data.model.User
import com.example.expense.ui.dialog.MonthYearPickerDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val utils: Utils,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline

    fun checkNetwork() { _isOffline.value = !networkMonitor.isOnline() }

    private val _homeState = MutableStateFlow<UiState<ExpenseSummaryResponse>>(UiState.Idle)
    val homeState = _homeState
    val formats = utils.getCurrentMonthFormats()
    val apiMonth = formats["api"]
    val display = formats["full"]
    val selectedMonth = MutableLiveData(apiMonth)

    val totalSpent = MutableLiveData("₹20,000")
    val totalBudget = MutableLiveData("₹40,000")
    val progress = MutableLiveData(50)
    val remainingAmount = MutableLiveData("Remaining\n ₹20,000")
    val weekSpent = MutableLiveData("This Week\n ₹3000")
    val userName = MutableLiveData("Good Morning \n Vikram 👋")
    val userInitials = MutableLiveData("?")

    /**
     * Fetches weekly summary once per session. Subsequent calls within the same
     * session return the cached result without hitting the network.
     * Pass forceRefresh = true on pull-to-refresh.
     */
    fun getWeeklySummary(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val result = repository.getWeeklySummary(forceRefresh)
            Log.d("ppp", result.toString())
            if (result is UiState.Success) {
                weekSpent.value = "The Week \n${result.data.data.totalSpent}"
            }
        }
    }

    fun getYearMonth(): MonthYearPickerDialog {
        val calendar = Calendar.getInstance()
        return MonthYearPickerDialog(
            selectedMonth = calendar.get(Calendar.MONTH),
            selectedYear = calendar.get(Calendar.YEAR),
            onSelected = { month, year ->
                val monthName = listOf(
                    "-01", "-02", "-03", "-04",
                    "-05", "-06", "-07", "-08",
                    "-09", "-10", "-11", "-12"
                )[month]
                selectedMonth.value = "$year$monthName"
                // Month picker is an explicit user action — always refetch
                getSummary(forceRefresh = true)
            }
        )
    }

    /**
     * Fetches summary for the selected month. Results are cached per month for
     * the session so navigating back to Home doesn't trigger a new API call.
     * Pass forceRefresh = true on pull-to-refresh.
     */
    fun getSummary(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isOffline.value = !networkMonitor.isOnline()
            if (_isOffline.value) {
                _homeState.value = UiState.Idle
                return@launch
            }
            _homeState.value = UiState.Loading
            val parm = selectedMonth.value
            val result = repository.getSummary(parm, forceRefresh)

            if (result is UiState.Error) {
                _isOffline.value = true
                _homeState.value = UiState.Idle
                return@launch
            }
            homeState.value = result

            if (result is UiState.Success) {
                totalSpent.value = "₹ ${result.data.data.totalSpent}"
                val percentage = utils.calculatePercentage(
                    result.data.data.totalSpent, result.data.data.totalBudget
                )
                totalBudget.value = "${percentage}% to ${result.data.data.totalBudget}"
                progress.value = percentage
                remainingAmount.value =
                    "Remaining \n ₹ ${result.data.data.totalBudget - result.data.data.totalSpent}"
            }
        }
    }

    /**
     * Fetches user profile once per session; returns cached data on revisits.
     */
    fun userProfileDetail(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val result = repository.getUserProfileDetail(forceRefresh)
            userDetailState.value = result
            Log.d("vvv", result.toString())

            if (result is UiState.Success) {
                val name = result.data.data?.name.orEmpty()
                userName.value = "${getGreeting()}\n$name 👋"
                userInitials.value = name.split(" ").take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                    .ifEmpty { "?" }
            }
        }
    }

    private val _userDetailState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)
    val userDetailState = _userDetailState

    fun setProgressWithAnimation(progressBar: ProgressBar, value: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, value)
        animator.duration = 800
        animator.start()
    }

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11  -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else      -> "Good Night"
        }
    }
}

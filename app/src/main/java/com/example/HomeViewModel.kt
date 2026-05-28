package com.example

import android.R
import android.animation.ObjectAnimator
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Utlity.Utils
import com.example.data.model.ApiResponse
import com.example.data.model.ByCategory
import com.example.data.model.ExpenseSummaryResponse
import com.example.data.model.Repository
import com.example.data.UiState
import com.example.data.model.User
import com.example.expense.ui.dialog.MonthYearPickerDialog
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.format
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository, private val utils: Utils) : ViewModel() {



    private val _homeState = MutableStateFlow<UiState<ExpenseSummaryResponse>>(UiState.Idle)
    val homeState = _homeState
    val formats = utils.getCurrentMonthFormats()
    val apiMonth = formats["api"]          // 2026-04
    val display = formats["full"]          // April 2026
    val selectedMonth = MutableLiveData(apiMonth)

    val totalSpent = MutableLiveData("₹20,000")
    val totalBudget = MutableLiveData("₹40,000")
    val progress = MutableLiveData(50)

    val remainingAmount = MutableLiveData("Remaining\n ₹20,000")

    val weekSpent = MutableLiveData("This Week\n ₹3000")

    val userName = MutableLiveData("Good Morning \n Vikram \uD83D\uDC4B")

    fun getWeeklySummary(){
        viewModelScope.launch {
          val  result = repository.getWeeklySummary()
            Log.d("ppp",result.toString())

            if(result is UiState.Success){

                weekSpent.value = "The Week \n${result.data.data.totalSpent}"



            }

        }
    }




    fun getYearMonth(): MonthYearPickerDialog{
        val calendar = Calendar.getInstance()

        return MonthYearPickerDialog(
            selectedMonth = calendar.get(Calendar.MONTH),
            selectedYear = calendar.get(Calendar.YEAR),
            onSelected = { month, year ->
                // ✅ Update ViewModel
//                    vm.setSelectedMonth(month, year)

                // ✅ Update TextView
                val monthName = listOf(
                    "-01", "-02", "-03", "-04",
                    "-05", "-06", "-07", "-08",
                    "-09", "-10", "-11", "-12"
                )[month]
                selectedMonth.value =  "$year$monthName"
                getSummary()
            }
        )

    }
    fun getSummary(){
        viewModelScope.launch {
            _homeState.value = UiState.Loading
            val parm  = selectedMonth.value
            val result = repository.getSummary(parm)

            homeState.value = result

            if (result is UiState.Success) {
                totalSpent.value = "₹ ${result.data.data.totalSpent}"

                val percentage = utils.calculatePercentage(result.data.data.totalSpent,result.data.data.totalBudget)
                Log.d("ppp" , result.data.data.totalSpent.toString())
                Log.d("ppp",result.data.data.totalBudget.toString())
                Log.d("ppp",percentage.toString())

                totalBudget.value = "${percentage}% to ${result.data.data.totalBudget}"
                Log.d("ppp",totalBudget.value.toString())

                progress.value = percentage

                val remaining = "₹ ${result.data.data.totalBudget - result.data.data.totalSpent}"

                remainingAmount.value =  "Remaining \n ${remaining}"
                
            }

        }
    }
    private val _userDetailState = MutableStateFlow<UiState<ApiResponse<User>>>(UiState.Idle)
    val userDetailState = _userDetailState
    fun userProfileDetail(){
        viewModelScope.launch {
            val result = repository.getUserProfileDetail()
            userDetailState.value = result
            Log.d("vvv",result.toString()+"dfgh")

            if(result is UiState.Success){
                userName.value = "${getGreeting()}\n${result.data.data?.name} \uD83D\uDC4B"
                Log.d("vvv",result.data.data?.name.toString())

            }




        }
    }








    // b.progress.progress = 75
    //        setProgressWithAnimation(progressBar = b.progress, 80)

    fun setProgressWithAnimation(progressBar: ProgressBar, value: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, value)
        animator.duration = 800
        animator.start()
    }

    fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11  -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else      -> "Good Night"
        }
    }







}
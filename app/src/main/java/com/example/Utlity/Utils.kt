package com.example.Utlity

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.example.ExpenseListItem
import com.example.data.model.AddExpenseRequest
import com.example.data.model.ExpenseX

import com.example.data.model.LoginUserRequest
import com.example.data.model.RegisterUserRequest
import com.example.data.UiState
import com.example.expense.AddExpenseFragment
//import com.google.android.libraries.places.api.model.LocalDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import java.time.LocalDate
//import java.time.format.DateTimeFormatter

import java.time.LocalDateTime
import kotlin.math.roundToInt


class Utils @Inject constructor() {

    fun validateUserRegInput(registerUserRequest: RegisterUserRequest): Pair<Boolean, String>{
        return when{
            (TextUtils.isEmpty(registerUserRequest.name)) || TextUtils.isEmpty(registerUserRequest.email) || TextUtils.isEmpty(registerUserRequest.password) ->{
                Pair(false, "please provide Credentials")
            }
            !Patterns.EMAIL_ADDRESS.matcher(registerUserRequest.email).matches() -> {
                Pair(false, "Please provide valid email")
            }
            registerUserRequest.password.length <= 5 -> {
                Pair(false, "Please length should be greatar then 5")
            }
            else-> {
                Pair(true, "")
            }



        }
    }

    fun validateExpenseInput(request: AddExpenseRequest): Pair<Boolean, String> {

        return when {
//            request.amount <= 0 ->
//                Pair(false, "Enter valid amount")

            request.categoryId.isBlank() ->
                Pair(false, "Select category")

            request.date.isBlank() ->
                Pair(false, "Select date")

            request.note.isBlank() ->
                Pair(false, "Note is required")

//            request.currency.isBlank() ->
//                Pair(false, "Currency is required")

            else ->
                Pair(true, "")
        }
    }
    companion object {

        fun showDateTimePicker(
            context: Context,
            onDateSelected: (String) -> Unit
        ) {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, day ->

                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hour, minute ->

                            val selectedCalendar = Calendar.getInstance()
                            selectedCalendar.set(year, month, day, hour, minute)

                            val format = SimpleDateFormat(
                                "yyy-MM-dd, hh:mm a",
                                Locale.getDefault()
                            )

                            onDateSelected(format.format(selectedCalendar.time))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                    )

                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun showDateTimePicker(context: Context, onDateTimeSelected: (String) -> Unit) {
//
//        val calendar = Calendar.getInstance()
//
//        // Date Picker
//        val datePicker = DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//
//                // Set selected date
//                calendar.set(Calendar.YEAR, year)
//                calendar.set(Calendar.MONTH, month)
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//                // Time Picker after Date
//                val timePicker = TimePickerDialog(
//                    context,
//                    { _, hourOfDay, minute ->
//
//                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                        calendar.set(Calendar.MINUTE, minute)
//
//                        // Format Date + Time
//                        val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                        val formattedDate = format.format(calendar.time)
//
//                        onDateTimeSelected(formattedDate)
//
//                    },
//                    calendar.get(Calendar.HOUR_OF_DAY),
//                    calendar.get(Calendar.MINUTE),
//                    false
//                )
//
//                timePicker.show()
//
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//
//        datePicker.show()
//    }


    fun validateUserLoginInput(loginUserRequest: LoginUserRequest): Pair<Boolean, String>{
        return when{
            (TextUtils.isEmpty(loginUserRequest.email)|| TextUtils.isEmpty(loginUserRequest.password))->{
                Pair(false, "Please provide Credentials")
            }
            !Patterns.EMAIL_ADDRESS.matcher(loginUserRequest.email).matches() -> {
                Pair(false, "Please provide valid email")
            }
            else->{
                Pair(true, "")
            }
        }
    }

    fun setProgressWithAnimation(progressBar: ProgressBar, value: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, value)
        animator.duration = 800
        animator.start()
    }

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> retrofit2.Response<T>
    ): UiState<T> {

        return try {
            val response = apiCall()

            if (response.isSuccessful && response.body() != null) {
                UiState.Success(response.body()!!)
            } else {
                UiState.Error(response.message())
            }

        } catch (e: Exception) {
            UiState.Error(e.message ?: "No Internet / Server Error")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapExpenses(
        expenses: List<ExpenseX>
    ): List<ExpenseListItem>{

        val result = mutableListOf<ExpenseListItem>()

        val sortedExpenses =
            expenses.sortedByDescending {
                parseDate(it.date)
            }

        val grouped = HashMap<String, MutableList<ExpenseX>>()

        for(expense in sortedExpenses){

            val key =
                getSectionTitle(
                    expense.date
                )

            if(grouped.containsKey(key)){

                grouped[key]?.add(
                    expense
                )

            }else{

                grouped[key] =
                    mutableListOf(
                        expense
                    )
            }
        }


//        val grouped = sortedExpenses.groupBy {
//            getSectionTitle(it.date)
//        }
        Log.d("nnn",grouped.toString())

        for ((title,expenseList) in grouped){
            result.add(ExpenseListItem.Header(title))
            for (i in expenseList){
                result.add(ExpenseListItem.ExpenseItem(i))
            }
        }

        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSectionTitle(dateString:String): String {

        val input = dateString.trim()

        val parsedDate = parseDate(input)
            ?: return "Older"

        val today = LocalDate.now()

        return when {
            parsedDate.isEqual(today) -> "Today"

            parsedDate.isEqual(
                today.minusDays(1)
            ) -> "Yesterday"

            else ->
                parsedDate.format(
                    DateTimeFormatter.ofPattern(
                        "dd MMM yyyy"
                    )
                )
        }
    }
    fun parseDate(input:String): LocalDate? {

        val localDatePatterns = listOf(
            "yyyy-MM-dd",
            "MMMM d, yyyy"
        )

        for(pattern in localDatePatterns){
            try{
                return LocalDate.parse(
                    input,
                    DateTimeFormatter.ofPattern(pattern)
                )
            }catch(_:Exception){}
        }

        val dateTimePatterns = listOf(

            "dd MMM yyyy, hh:mm a",   //25 Apr 2026, 06:35 PM

            "yyyy-MM-dd, hh:mm a",    //2026-04-29, 03:29 PM

            "yyyy-MM-dd'T'HH:mm:ss",

            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        )

        for(pattern in dateTimePatterns){
            try{
                return LocalDateTime.parse(
                    input,
                    DateTimeFormatter.ofPattern(pattern)
                ).toLocalDate()

            }catch(_:Exception){}
        }

        return null
    }

    fun calculatePercentage(value: Int, total: Int): Int {
        return if (total == 0) 0
        else ((value.toFloat() / total.toFloat()) * 100).roundToInt()
    }



    fun getCurrentMonthFormats(): Map<String, String> {

        val calendar = Calendar.getInstance()

        return mapOf(
            "api" to SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time),
            "full" to SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time),
            "short" to SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(calendar.time),
            "numeric" to SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.time)
        )
//        val apiMonth = formats["api"]          // 2026-04
//        val display = formats["full"]          // April 2026
//        val short = formats["short"]           // Apr 2026
//        val numeric = formats["numeric"]       // 04/2026
    }




}
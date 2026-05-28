package com.example

import com.example.data.model.ApiResponse
import com.example.data.model.ExpenseSummaryResponse
import com.example.data.model.ExpenseWeeklySummaryResponse
import com.example.data.model.ExpensesResponse
import com.example.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface   HomeApiInterface {

    @GET("api/analytics/summary")
    suspend fun getSummary(@Query("month") month: String? ): Response<ExpenseSummaryResponse>

    @GET("api/analytics/weekly")
    suspend fun getWeeklySummary(): Response<ApiResponse<ExpenseWeeklySummaryResponse>>

    @GET("api/expenses")
    suspend fun getExpenses(@Query("page") page: Int, @Query("limit") limit: Int): Response<ExpensesResponse>


    @GET("/api/user/profile")
    suspend fun getProfileDetail(): Response<ApiResponse<User>>



}
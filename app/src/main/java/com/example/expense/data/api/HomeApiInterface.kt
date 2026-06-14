package com.example.expense.data.api

import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.ChangePasswordRequest
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.ExpenseSummaryResponse
import com.example.expense.data.model.ExpenseWeeklySummaryResponse
import com.example.expense.data.model.ExpensesResponse
import com.example.expense.data.model.UpdateProfileRequest
import com.example.expense.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface HomeApiInterface {

    @GET("api/analytics/summary")
    suspend fun getSummary(@Query("month") month: String?): Response<ExpenseSummaryResponse>

    @GET("api/analytics/weekly")
    suspend fun getWeeklySummary(): Response<ApiResponse<ExpenseWeeklySummaryResponse>>

    @GET("api/expenses")
    suspend fun getExpenses(@Query("page") page: Int, @Query("limit") limit: Int): Response<ExpensesResponse>

    @GET("/api/user/profile")
    suspend fun getProfileDetail(): Response<ApiResponse<User>>

    @PUT("/api/user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ApiResponse<User>>

    @PUT("/api/user/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponse<DeleteResponse>>
}
package com.example.expense.data.api

import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.BulkBudgetSyncRequest
import com.example.expense.data.model.BulkSyncResponse
import com.example.expense.data.model.SetBudgetRequest
import com.example.expense.data.model.setBudgetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BudgetInterface {

    @GET("/api/budgets")
    suspend fun getBudgets(@Query("month") month: String?): Response<ApiResponse<List<BudgetData>>>

    @POST("/api/budgets")
    suspend fun setBudget(@Body setBudgetRequest: SetBudgetRequest): Response<ApiResponse<setBudgetResponse>>

    @POST("/api/budgets/bulk")
    suspend fun setBudgetsBulk(@Body request: BulkBudgetSyncRequest): Response<ApiResponse<BulkSyncResponse>>
}
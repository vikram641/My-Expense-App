package com.example

import com.example.data.model.ApiResponse
import com.example.data.model.BudgetData
import com.example.data.model.SetBudgetRequest
import com.example.data.model.setBudgetResponse
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
}
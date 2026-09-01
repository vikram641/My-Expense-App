package com.example.expense.data.api

import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BudgetSyncDataRequest
import com.example.expense.data.model.BudgetSynceDataResponse
import com.example.expense.data.model.ExpenseDatSyncRequest
import com.example.expense.data.model.ExpenseSynceDataResponse
import com.example.expense.data.model.GetExpenseSyncDataResponse
import com.example.expense.data.model.GetSyncBudgetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DataSyncApiInterface {

    @POST("/api/expenses/sync")
    suspend fun syncExpense(@Body  expenseDatSyncRequest: ExpenseDatSyncRequest): Response<ApiResponse<ExpenseSynceDataResponse>>

    @POST("/api/budgets/sync")
    suspend fun syncBudget(@Body budgetSyncDataRequest: BudgetSyncDataRequest): Response<ApiResponse<BudgetSynceDataResponse>>

    @GET("/api/budgets/last-two-months")
    suspend fun getSyncForBudget(): Response<ApiResponse<GetSyncBudgetResponse>>


    @GET("/api/expenses/last-two-months")
    suspend fun getSyncForExpense(): Response<ApiResponse<GetExpenseSyncDataResponse>>
}
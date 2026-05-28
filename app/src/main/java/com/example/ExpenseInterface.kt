package com.example

import com.example.data.model.AddExpenseRequest
import com.example.data.model.AddExpenseResponse
import com.example.data.model.ApiResponse
import com.example.data.model.CatDataResponse
import com.example.data.model.ExpenseX
import com.example.data.Api.SearchApiData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ExpenseInterface {

    @GET("/api/categories")
    suspend fun getExpenseCat(): Response<ApiResponse<List<CatDataResponse>>>

    @POST("/api/expenses")
    suspend fun addExpense(@Body addExpenseRequest: AddExpenseRequest): Response<ApiResponse<AddExpenseResponse>>

    @GET("api/expenses")
    suspend fun getExpenses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("category") category: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<SearchApiData>>
}
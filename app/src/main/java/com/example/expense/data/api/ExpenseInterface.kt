package com.example.expense.data.api

import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BulkExpenseSyncRequest
import com.example.expense.data.model.BulkSyncResponse
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.SearchApiData
import com.example.expense.data.model.UpdateExpenseRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @GET("/api/expenses/{id}")
    suspend fun getExpenseDetail(@Path("id") id: String): Response<ApiResponse<ExpenseX>>

    @PUT("/api/expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: String,
        @Body request: UpdateExpenseRequest
    ): Response<ApiResponse<ExpenseX>>

    @DELETE("/api/expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<ApiResponse<DeleteResponse>>

    @POST("/api/expenses/bulk")
    suspend fun addExpensesBulk(@Body request: BulkExpenseSyncRequest): Response<ApiResponse<BulkSyncResponse>>
}
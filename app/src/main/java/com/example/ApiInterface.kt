package com.example

import com.example.data.model.ExpenseSummaryData
import com.example.data.model.ExpenseSummaryResponse
import com.example.data.model.LoginUserRequest
import com.example.data.model.LoginUserResponse
import com.example.data.model.RegisterUserRequest
import com.example.data.model.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("/api/auth/register")
    suspend fun registerUser(@Body registerUserRequest: RegisterUserRequest): Response<RegisterUserResponse>


    @POST("/api/auth/login")
    suspend fun loginUser(@Body loginUserRequest: LoginUserRequest): Response<LoginUserResponse>


}
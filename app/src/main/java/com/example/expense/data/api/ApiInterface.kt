package com.example.expense.data.api

import com.example.expense.data.model.LoginUserRequest
import com.example.expense.data.model.LoginUserResponse
import com.example.expense.data.model.LogoutRequest
import com.example.expense.data.model.RegisterUserRequest
import com.example.expense.data.model.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("/api/auth/register")
    suspend fun registerUser(@Body registerUserRequest: RegisterUserRequest): Response<RegisterUserResponse>

    @POST("/api/auth/login")
    suspend fun loginUser(@Body loginUserRequest: LoginUserRequest): Response<LoginUserResponse>

    @POST("/api/auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): Response<Unit>
}
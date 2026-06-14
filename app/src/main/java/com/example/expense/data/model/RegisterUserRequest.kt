package com.example.expense.data.model

data class RegisterUserRequest(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val currency: String = "INR"
)
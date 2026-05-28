package com.example.data.model

data class RegisterUserRequest(
    val email: String = "",
    val name: String = "",
    val password: String = ""
)
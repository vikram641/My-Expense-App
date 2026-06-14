package com.example.expense.data.model

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

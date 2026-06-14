package com.example.expense.data.model

import com.example.expense.data.model.UserTokenData

data class LoginUserResponse(
    val `data`: UserTokenData,
    val success: Boolean
)
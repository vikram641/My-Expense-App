package com.example.data.model

import com.example.data.model.UserTokenData

data class LoginUserResponse(
    val `data`: UserTokenData,
    val success: Boolean
)
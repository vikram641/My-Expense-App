package com.example.data.model

data class UserTokenData(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val user: User
)
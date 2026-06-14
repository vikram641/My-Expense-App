package com.example.expense.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("email")
    val email: String? = "",

    @SerializedName("currency")
    val currency: String = "",

    @SerializedName("fcmToken")
    val fcmToken: String? = "",

    @SerializedName("createdAt")
    val createdAt: String? = ""
)
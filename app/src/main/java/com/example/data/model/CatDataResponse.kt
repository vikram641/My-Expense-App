package com.example.data.model

data class CatDataResponse(
    val color: String? = " ",
    val icon: String? = "",
    val id: String? = "",
    val isDefault: Boolean?=false,
    val name: String?= "",
    val isSelected : Boolean? = false
)
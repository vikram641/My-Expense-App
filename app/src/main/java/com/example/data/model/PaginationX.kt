package com.example.data.model

data class PaginationX(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)
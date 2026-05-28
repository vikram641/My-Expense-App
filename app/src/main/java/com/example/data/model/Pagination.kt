package com.example.data.model

data class Pagination(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)
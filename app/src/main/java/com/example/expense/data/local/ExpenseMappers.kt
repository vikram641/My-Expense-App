package com.example.expense.data.local

import com.example.expense.data.model.CategoryX
import com.example.expense.data.model.ExpenseX

/** Domain (API) -> Room entity */
fun ExpenseX.toEntity(): ExpenseEntity =
    ExpenseEntity(
        id = id,
        amount = amount,
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        categoryId = category.id,
        categoryName = category.name,
        categoryColor = category.color,
        categoryIcon = category.icon
    )

/** Room entity -> Domain (API) */
fun ExpenseEntity.toDomain(): ExpenseX =
    ExpenseX(
        id = id,
        amount = amount,
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt,
        category = CategoryX(
            color = categoryColor,
            icon = categoryIcon,
            id = categoryId,
            name = categoryName
        )
    )

fun List<ExpenseX>.toEntityList(): List<ExpenseEntity> = map { it.toEntity() }
fun List<ExpenseEntity>.toDomainList(): List<ExpenseX> = map { it.toDomain() }

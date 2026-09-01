package com.example.expense.data.local

import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.CategoryX
import com.example.expense.data.model.Expense
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.ExpenseXX
import java.util.UUID

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
        categoryIcon = category.icon,
        userId = userId

    )
fun ExpenseEntity.toDomain1(): ExpenseXX{
    return ExpenseXX(
        id = id,
        categoryName = categoryName,
        categoryIcon = categoryIcon,
        categoryColor = categoryColor,
        currency = currency,
        categoryId = categoryId,
        note = note,
        amount = amount,
        receiptUrl = receiptUrl,
        date = date
    )
}

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
        userId = userId,
        category = CategoryX(
            color = categoryColor,
            icon = categoryIcon,
            id = categoryId,
            name = categoryName
        )
    )

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount.toString(),
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl?.toString() ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt,

        categoryId = category.id,
        categoryName = category.name,
        categoryColor = category.color,
        categoryIcon = category.icon
    )
}
fun List<Expense>.toEntityListExpenses(): List<ExpenseEntity> {
    return map { it.toEntity() }
}

fun AddExpenseRequest.toEntity(): ExpenseEntity =
    ExpenseEntity(
        id = UUID.randomUUID().toString(), // server id baad me milegi
        amount = amount,
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl,
        createdAt = "",
        updatedAt = "",
        categoryId = categoryId,
        categoryName = categoryName,
        categoryColor = categoryColor,
        categoryIcon = categoryIcon,
        userId = userId
    )

fun List<ExpenseX>.toEntityList(): List<ExpenseEntity> = map { it.toEntity() }
fun List<ExpenseEntity>.toDomainList(): List<ExpenseX> = map { it.toDomain() }

fun List<ExpenseEntity>.toDomainList1(): List<ExpenseXX>  = map{ it.toDomain1() }

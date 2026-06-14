package com.example.expense.data.local

import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.SetBudgetRequest

fun BudgetData.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryColor = categoryColor,
    currency = currency,
    limitAmount = limitAmount,
    month = month,
    spentAmount = spentAmount
)

fun BudgetEntity.toDomain(): BudgetData = BudgetData(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryColor = categoryColor,
    currency = currency,
    limitAmount = limitAmount,
    month = month,
    spentAmount = spentAmount
)

fun List<BudgetData>.toEntityList(): List<BudgetEntity> = map { it.toEntity() }
fun List<BudgetEntity>.toDomainList(): List<BudgetData> = map { it.toDomain() }

fun SetBudgetRequest.toPendingEntity(): PendingBudgetEntity = PendingBudgetEntity(
    categoryId = categoryId,
    currency = currency,
    limitAmount = limitAmount,
    month = month
)

fun PendingBudgetEntity.toRequest(): SetBudgetRequest = SetBudgetRequest(
    categoryId = categoryId,
    currency = currency,
    limitAmount = limitAmount,
    month = month
)

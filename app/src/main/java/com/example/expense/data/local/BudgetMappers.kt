package com.example.expense.data.local

import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.Budget
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.BudgetSyncDataRequest
import com.example.expense.data.model.BudgetX
import com.example.expense.data.model.SetBudgetRequest
import java.util.UUID

fun BudgetData.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryColor = categoryColor,
    currency = currency,
    limitAmount = limitAmount,
    month = month,
    spentAmount = spentAmount,
    categoryIcon = ""
)

fun SetBudgetRequest.toEntity(): BudgetEntity =
    BudgetEntity(
        id = UUID.randomUUID().toString(),
        currency = currency,
        limitAmount = limitAmount,
        month = month,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryColor = categoryColor,
        spentAmount = spendAmount,
        categoryIcon = categoryIcon

    )

fun BudgetEntity.toDomain1(): Budget {
    return Budget(
        id = id,
        currency = currency,
        categoryColor = categoryColor,
        categoryId = categoryId,
        categoryIcon = categoryIcon,
        categoryName = categoryName,
        month = month,
        limitAmount = limitAmount,
        spentAmount = spentAmount
    )
}


fun BudgetEntity.toDomain(): BudgetData = BudgetData(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryColor = categoryColor,
    currency = currency,
    limitAmount = limitAmount,
    month = month,
    spentAmount = spentAmount,
    categoryIcon = categoryIcon
)

fun List<BudgetData>.toEntityList(): List<BudgetEntity> = map { it.toEntity() }
fun List<BudgetEntity>.toDomainList(): List<BudgetData> = map { it.toDomain() }
fun List<BudgetEntity>.toDomainList1(): List<Budget> = map { it.toDomain1() }

fun BudgetX.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryColor = categoryColor,
    currency = currency,
    limitAmount = limitAmount,
    month = month,
    spentAmount = spentAmount,
    categoryIcon = categoryIcon
)

fun List<BudgetX>.toEntityListX(): List<BudgetEntity> = map { it.toEntity() }

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

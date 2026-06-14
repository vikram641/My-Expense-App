package com.example.expense.data.local

import com.example.expense.data.model.AddExpenseRequest

/** Queue a request for later sync. */
fun AddExpenseRequest.toPendingEntity(): PendingExpenseEntity =
    PendingExpenseEntity(
        amount = amount,
        categoryId = categoryId,
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl
    )

/** Rebuild the API request when draining the queue. */
fun PendingExpenseEntity.toRequest(): AddExpenseRequest =
    AddExpenseRequest(
        amount = amount,
        categoryId = categoryId,
        currency = currency,
        date = date,
        note = note,
        receiptUrl = receiptUrl
    )

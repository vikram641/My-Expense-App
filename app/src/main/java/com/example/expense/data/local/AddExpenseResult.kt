package com.example.expense.data.local

import com.example.expense.data.model.AddExpenseResponse

/** Outcome of an offline-first "add expense". */
sealed class AddExpenseResult {
    /** Reached the server and saved. */
    data class Synced(val data: AddExpenseResponse) : AddExpenseResult()

    /** Saved locally; will sync when back online. */
    object Queued : AddExpenseResult()

    /** Server was reachable but rejected the request (won't auto-queue). */
    data class Failed(val message: String) : AddExpenseResult()
}

/** Outcome of draining the pending queue. */
data class SyncResult(
    val synced: Int,
    val remaining: Int
)

/** Generic result for offline-first write operations (budget, etc.). */
sealed class OperationResult {
    object Queued : OperationResult()
    object Synced : OperationResult()
    data class Failed(val message: String) : OperationResult()
}

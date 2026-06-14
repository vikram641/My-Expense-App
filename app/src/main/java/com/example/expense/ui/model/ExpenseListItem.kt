package com.example.expense.ui.model

import com.example.expense.data.model.ExpenseX

sealed class ExpenseListItem {
    data class Header(
        val title:String
    ): ExpenseListItem()

    data class ExpenseItem(
        val expense: ExpenseX
    ): ExpenseListItem()
}

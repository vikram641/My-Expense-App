package com.example

import com.example.data.model.ExpenseX

sealed class ExpenseListItem {
    data class Header(
        val title:String
    ): ExpenseListItem()

    data class ExpenseItem(
        val expense: ExpenseX
    ): ExpenseListItem()
}

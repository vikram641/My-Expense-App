package com.example.expense.core.util

object CurrencyConstants {
    val CURRENCIES = listOf(
        "INR - Indian Rupee",
        "USD - US Dollar",
        "EUR - Euro",
        "GBP - British Pound",
        "JPY - Japanese Yen",
        "CAD - Canadian Dollar",
        "AUD - Australian Dollar",
        "SGD - Singapore Dollar",
        "AED - UAE Dirham",
        "CNY - Chinese Yuan",
        "CHF - Swiss Franc",
        "KRW - South Korean Won",
        "MXN - Mexican Peso",
        "BRL - Brazilian Real"
    )

    fun getCode(displayValue: String): String =
        displayValue.substringBefore(" -").trim().ifEmpty { displayValue }

    fun getDisplayForCode(code: String): String =
        CURRENCIES.firstOrNull { it.startsWith("$code ") } ?: code
}

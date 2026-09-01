package com.example.expense.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.util.OnboardingPrefs
import com.example.expense.core.util.Utils
import com.example.expense.data.local.BudgetDao
import com.example.expense.data.local.BudgetEntity
import com.example.expense.data.local.CategoryDao
import com.example.expense.data.local.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

/** Activity-scoped state shared across the onboarding screens as the user fills them in. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPrefs: OnboardingPrefs,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val utils: Utils
) : ViewModel() {

    var name: String = ""
    var monthlyBudget: Int = 25000
    var currencyCode: String = "INR"

    /** Selected on SetupBudgetFragment - which categories to create a starting budget for. */
    val selectedCategoryIds: MutableSet<String> = mutableSetOf()

    /** Firestore-synced category list (see CLAUDE.md "Categories now come from Firestore"),
     * offered as chips on SetupBudgetFragment so the user can opt a few into tracking. */
    val categories: StateFlow<List<CategoryEntity>> =
        categoryDao.observeCategories().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Saves the collected profile, then - for each category picked on SetupBudgetFragment -
     * creates a real budgets-table entry for the current month (the "make entry and pass
     * current date time" ask: this table's date dimension is the yyyy-MM `month` column,
     * from Utils.getCurrentMonthFormats(), same helper the rest of the app uses for it).
     * Closes the gap CLAUDE.md flagged: onboarding's budget number never used to reach the
     * real per-category BudgetEntity/Room table the Budget screen actually reads from.
     */
    suspend fun completeOnboarding() {
        onboardingPrefs.saveProfile(
            name = name.trim().ifEmpty { "there" },
            monthlyBudget = monthlyBudget,
            currencyCode = currencyCode
        )

        if (selectedCategoryIds.isEmpty()) return
        val month = utils.getCurrentMonthFormats()["api"] ?: return
        val perCategoryAmount = monthlyBudget / selectedCategoryIds.size
        val picked = categories.value.filter { it.id in selectedCategoryIds }
        budgetDao.upsertBudgets(
            picked.map { category ->
                BudgetEntity(
                    id = UUID.randomUUID().toString(),
                    categoryId = category.id,
                    categoryName = category.name,
                    categoryColor = category.color,
                    currency = currencyCode,
                    limitAmount = perCategoryAmount,
                    month = month,
                    spentAmount = 0,
                    categoryIcon = category.icon
                )
            }
        )
    }
}

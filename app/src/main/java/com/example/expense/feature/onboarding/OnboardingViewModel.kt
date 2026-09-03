package com.example.expense.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.util.OnboardingPrefs
import com.example.expense.core.util.Utils
import com.example.expense.data.local.BudgetDao
import com.example.expense.data.local.BudgetEntity
import com.example.expense.data.local.CategoryDao
import com.example.expense.data.local.CategoryEntity
import com.example.expense.data.repository.Repository
import com.example.expense.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/** Activity-scoped state shared across the onboarding screens as the user fills them in. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPrefs: OnboardingPrefs,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val repository: Repository,
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

    init {
        // Categories only ever land in Room the first time something calls
        // Repository.getExpenseCat() (see CLAUDE.md "Categories now come from Firestore") -
        // on a truly fresh install nothing has done that yet, so SetupBudgetFragment's chip
        // picker would render empty and the user couldn't select anything. Kick the fetch off
        // as soon as onboarding starts so categories are ready several screens later, by the
        // time the user reaches SetupBudgetFragment.
        viewModelScope.launch { loadCategoriesWithRetry() }
    }

    /**
     * getExpenseCat() is a single Firestore call with no retry - on a fresh install this races
     * the app's own Firebase init and any first-request network latency, so it fails silently
     * on a fraction of cold starts and the chip picker was permanently empty for that session
     * (the "category list sometimes doesn't show" bug). Retry a few times with a short delay
     * before giving up; SetupBudgetFragment also calls retryLoadCategoriesIfEmpty() as a second
     * chance in case all of these attempts land before the network is actually up.
     */
    private suspend fun loadCategoriesWithRetry() {
        repeat(3) { attempt ->
            if (repository.getExpenseCat() is UiState.Success) return
            if (attempt < 2) delay(1500)
        }
    }

    /** Called from SetupBudgetFragment.onViewCreated() - cheap no-op if categories already loaded. */
    fun retryLoadCategoriesIfEmpty() {
        if (categories.value.isEmpty()) {
            viewModelScope.launch { loadCategoriesWithRetry() }
        }
    }

    /**
     * Saves the collected profile, then creates real budgets-table entries for the current
     * month (the yyyy-MM `month` column, from Utils.getCurrentMonthFormats(), same helper the
     * rest of the app uses for it) so the number just collected actually reaches the Budget
     * screen and Home's budget card instead of sitting unused in OnboardingPrefs.
     *
     * - Categories explicitly picked on SetupBudgetFragment split the budget between them.
     * - If the user didn't pick any (chips never tapped), default to tracking every loaded
     *   category rather than silently dropping the number - opt-out beats losing data.
     * - If categories never loaded at all (e.g. first run, fully offline), fall back to a
     *   single "Overall Budget" entry so the entered amount is never lost.
     */
    suspend fun completeOnboarding() {
        onboardingPrefs.saveProfile(
            name = name.trim().ifEmpty { "there" },
            monthlyBudget = monthlyBudget,
            currencyCode = currencyCode
        )

        val month = utils.getCurrentMonthFormats()["api"] ?: return
        val picked = categories.value.filter { it.id in selectedCategoryIds }
        val targets = picked.ifEmpty { categories.value }

        if (targets.isEmpty()) {
            budgetDao.insertBudget(
                BudgetEntity(
                    id = UUID.randomUUID().toString(),
                    categoryId = "general",
                    categoryName = "Overall Budget",
                    categoryColor = "#7B61FF",
                    currency = currencyCode,
                    limitAmount = monthlyBudget,
                    month = month,
                    spentAmount = 0,
                    categoryIcon = "ic_wallet_outline"
                )
            )
            return
        }

        val perCategoryAmount = monthlyBudget / targets.size
        budgetDao.upsertBudgets(
            targets.map { category ->
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

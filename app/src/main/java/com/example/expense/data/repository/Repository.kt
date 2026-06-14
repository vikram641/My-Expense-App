package com.example.expense.data.repository

import com.example.expense.data.api.ApiInterface
import com.example.expense.data.api.BudgetInterface
import com.example.expense.data.api.ExpenseInterface
import com.example.expense.data.api.HomeApiInterface
import com.example.expense.core.util.SessionCacheManager
import com.example.expense.core.util.Utils
import com.example.expense.data.local.AddExpenseResult
import com.example.expense.data.local.BudgetDao
import com.example.expense.data.local.CategoryDao
import com.example.expense.data.local.ExpenseDao
import com.example.expense.data.local.OperationResult
import com.example.expense.data.local.PendingBudgetDao
import com.example.expense.data.local.PendingExpenseDao
import com.example.expense.data.local.SyncResult
import com.example.expense.data.local.toDomainList
import com.example.expense.data.local.toEntityList
import com.example.expense.data.local.toPendingEntity
import com.example.expense.data.local.toRequest
import com.example.expense.core.UiState
import com.example.expense.core.network.NetworkMonitor
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.BulkBudgetSyncRequest
import com.example.expense.data.model.BulkExpenseSyncRequest
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.ChangePasswordRequest
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.ExpenseQuery
import com.example.expense.data.model.ExpenseSummaryResponse
import com.example.expense.data.model.ExpenseWeeklySummaryResponse
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.ExpensesResponse
import com.example.expense.data.model.LoginUserRequest
import com.example.expense.data.model.LoginUserResponse
import com.example.expense.data.model.LogoutRequest
import com.example.expense.data.model.RegisterUserRequest
import com.example.expense.data.model.RegisterUserResponse
import com.example.expense.data.model.SearchApiData
import com.example.expense.data.model.SetBudgetRequest
import com.example.expense.data.model.UpdateExpenseRequest
import com.example.expense.data.model.UpdateProfileRequest
import com.example.expense.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: ApiInterface,
    private val homeApi: HomeApiInterface,
    private val budgetApi: BudgetInterface,
    private val expenseApi: ExpenseInterface,
    private val expenseDao: ExpenseDao,
    private val pendingExpenseDao: PendingExpenseDao,
    private val budgetDao: BudgetDao,
    private val pendingBudgetDao: PendingBudgetDao,
    private val categoryDao: CategoryDao,
    private val networkMonitor: NetworkMonitor,
    private val sessionCacheManager: SessionCacheManager
) {

    @Inject
    lateinit var utils: Utils

    private val gson = Gson()

    // ─── Auth ─────────────────────────────────────────────────────────────────

    suspend fun registerUser(request: RegisterUserRequest): UiState<RegisterUserResponse> =
        utils.safeApiCall { api.registerUser(request) }

    suspend fun loginUser(request: LoginUserRequest): UiState<LoginUserResponse> =
        utils.safeApiCall { api.loginUser(request) }

    suspend fun logout(refreshToken: String): UiState<Unit> =
        utils.safeApiCall { api.logout(LogoutRequest(refreshToken)) }

    // ─── Home / Summary (session-cached GET) ──────────────────────────────────

    /**
     * Returns session-cached summary first; only hits the network if this
     * month's data hasn't been fetched yet in the current app session.
     * Pass forceRefresh = true to bypass the cache (e.g. pull-to-refresh).
     */
    suspend fun getSummary(month: String?, forceRefresh: Boolean = false): UiState<ExpenseSummaryResponse> {
        val key = SessionCacheManager.homeSummaryKey(month)
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val json = sessionCacheManager.getString(key)
            if (json != null) {
                val cached = gson.fromJson(json, ExpenseSummaryResponse::class.java)
                return UiState.Success(cached)
            }
        }
        val result = utils.safeApiCall { homeApi.getSummary(month) }
        if (result is UiState.Success) {
            sessionCacheManager.putString(key, gson.toJson(result.data))
        }
        return result
    }

    /**
     * Weekly summary: fetched once per session and stored in SharedPreferences.
     */
    suspend fun getWeeklySummary(forceRefresh: Boolean = false): UiState<ApiResponse<ExpenseWeeklySummaryResponse>> {
        val key = SessionCacheManager.KEY_WEEKLY_SUMMARY
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val json = sessionCacheManager.getString(key)
            if (json != null) {
                val type = object : TypeToken<ApiResponse<ExpenseWeeklySummaryResponse>>() {}.type
                val cached = gson.fromJson<ApiResponse<ExpenseWeeklySummaryResponse>>(json, type)
                return UiState.Success(cached)
            }
        }
        val result = utils.safeApiCall { homeApi.getWeeklySummary() }
        if (result is UiState.Success) {
            sessionCacheManager.putString(key, gson.toJson(result.data))
        }
        return result
    }

    suspend fun getExpenses(page: Int, limit: Int): UiState<ExpensesResponse> =
        utils.safeApiCall { homeApi.getExpenses(page = page, limit = limit) }

    // ─── User Profile (session-cached GET) ────────────────────────────────────

    suspend fun getUserProfileDetail(forceRefresh: Boolean = false): UiState<ApiResponse<User>> {
        val key = SessionCacheManager.KEY_USER_PROFILE
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val json = sessionCacheManager.getString(key)
            if (json != null) {
                val type = object : TypeToken<ApiResponse<User>>() {}.type
                val cached = gson.fromJson<ApiResponse<User>>(json, type)
                return UiState.Success(cached)
            }
        }
        val result = utils.safeApiCall { homeApi.getProfileDetail() }
        if (result is UiState.Success) {
            sessionCacheManager.putString(key, gson.toJson(result.data))
        }
        return result
    }

    suspend fun updateUserProfile(request: UpdateProfileRequest): UiState<ApiResponse<User>> {
        val result = utils.safeApiCall { homeApi.updateProfile(request) }
        // Invalidate profile cache so next fetch reads updated data
        if (result is UiState.Success) sessionCacheManager.invalidate(SessionCacheManager.KEY_USER_PROFILE)
        return result
    }

    suspend fun changePassword(request: ChangePasswordRequest): UiState<ApiResponse<DeleteResponse>> =
        utils.safeApiCall { homeApi.changePassword(request) }

    // ─── Expenses (CRUD + offline cache) ──────────────────────────────────────

    suspend fun getExpenseDetail(id: String): UiState<ApiResponse<ExpenseX>> =
        utils.safeApiCall { expenseApi.getExpenseDetail(id) }

    suspend fun updateExpense(id: String, request: UpdateExpenseRequest): UiState<ApiResponse<ExpenseX>> =
        utils.safeApiCall { expenseApi.updateExpense(id, request) }

    suspend fun deleteExpense(id: String): UiState<ApiResponse<DeleteResponse>> =
        utils.safeApiCall { expenseApi.deleteExpense(id) }

    suspend fun addExpense(request: AddExpenseRequest): UiState<ApiResponse<AddExpenseResponse>> =
        utils.safeApiCall { expenseApi.addExpense(request) }

    suspend fun getExpensesSearch(query: ExpenseQuery): UiState<ApiResponse<SearchApiData>> =
        utils.safeApiCall {
            expenseApi.getExpenses(
                page = query.page,
                limit = query.limit,
                category = query.category,
                from = query.from,
                to = query.to,
                search = query.search
            )
        }

    // ─── Room: local expense cache ─────────────────────────────────────────────

    fun observeCachedExpenses(): Flow<List<ExpenseX>> =
        expenseDao.observeExpenses().map { it.toDomainList() }

    suspend fun cacheExpenses(expenses: List<ExpenseX>) =
        expenseDao.upsertExpenses(expenses.toEntityList())

    suspend fun clearExpenseCache() = expenseDao.clearAll()

    suspend fun refreshExpenses(query: ExpenseQuery): UiState<ApiResponse<SearchApiData>> {
        val result = getExpensesSearch(query)
        if (result is UiState.Success) cacheExpenses(result.data.data.expenses)
        return result
    }

    // ─── Offline expense add + bulk sync ──────────────────────────────────────

    fun observePendingExpenseCount(): Flow<Int> = pendingExpenseDao.observeCount()

    suspend fun addExpenseOfflineFirst(request: AddExpenseRequest): AddExpenseResult {
        if (!networkMonitor.isOnline()) {
            pendingExpenseDao.insert(request.toPendingEntity())
            return AddExpenseResult.Queued
        }
        return when (val res = addExpense(request)) {
            is UiState.Success -> AddExpenseResult.Synced(res.data.data)
            is UiState.Error   -> AddExpenseResult.Failed(res.message)
            else               -> AddExpenseResult.Failed("Unknown error")
        }
    }

    /**
     * Bulk-syncs all pending expenses in one request; falls back to one-by-one
     * if the bulk endpoint is unavailable or returns an error.
     */
    suspend fun syncPendingExpenses(): SyncResult {
        if (!networkMonitor.isOnline()) {
            return SyncResult(synced = 0, remaining = pendingExpenseDao.count())
        }
        val pending = pendingExpenseDao.getAll()
        if (pending.isEmpty()) return SyncResult(synced = 0, remaining = 0)

        val bulkResult = utils.safeApiCall {
            expenseApi.addExpensesBulk(BulkExpenseSyncRequest(pending.map { it.toRequest() }))
        }
        if (bulkResult is UiState.Success) {
            pendingExpenseDao.clearAll()
            return SyncResult(synced = pending.size, remaining = 0)
        }

        // Bulk endpoint unavailable — fall back to one-by-one FIFO
        var synced = 0
        for (item in pending) {
            val res = addExpense(item.toRequest())
            if (res is UiState.Success) {
                pendingExpenseDao.deleteById(item.localId)
                synced++
            } else break
        }
        return SyncResult(synced = synced, remaining = pendingExpenseDao.count())
    }

    // ─── Categories (session-cached GET, stored in Room) ──────────────────────

    /**
     * Serves categories from Room. Only fetches from the API once per session.
     */
    suspend fun getExpenseCat(forceRefresh: Boolean = false): UiState<ApiResponse<List<CatDataResponse>>> {
        val key = SessionCacheManager.KEY_CATEGORIES
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val cached = categoryDao.getAll()
            if (cached.isNotEmpty()) {
                return UiState.Success(ApiResponse(success = true, data = cached.toDomainList()))
            }
        }
        val result = utils.safeApiCall { expenseApi.getExpenseCat() }
        if (result is UiState.Success) {
            categoryDao.clearAll()
            categoryDao.upsertCategories(result.data.data.toEntityList())
            sessionCacheManager.markFetched(key)
        }
        return result
    }

    // ─── Budgets (session-cached GET + offline-first SET) ─────────────────────

    /**
     * Serves budget list for [month] from Room when already fetched this session.
     * On first access (or forceRefresh), fetches from API and caches in Room.
     */
    suspend fun getBudgets(month: String?, forceRefresh: Boolean = false): UiState<ApiResponse<List<BudgetData>>> {
        val key = SessionCacheManager.budgetKey(month)
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val cached = budgetDao.getBudgets(month ?: "")
            if (cached.isNotEmpty()) {
                return UiState.Success(ApiResponse(success = true, data = cached.toDomainList()))
            }
        }
        val result = utils.safeApiCall { budgetApi.getBudgets(month) }
        if (result is UiState.Success) {
            if (!month.isNullOrEmpty()) budgetDao.clearForMonth(month)
            else budgetDao.clearAll()
            budgetDao.upsertBudgets(result.data.data.toEntityList())
            sessionCacheManager.markFetched(key)
        }
        return result
    }

    /** Reactive stream of budgets for [month] directly from Room. */
    fun observeCachedBudgets(month: String): Flow<List<BudgetData>> =
        budgetDao.observeBudgets(month).map { it.toDomainList() }

    fun observePendingBudgetCount(): Flow<Int> = pendingBudgetDao.observeCount()

    /**
     * Offline-first budget SET:
     * - Offline → queued in pending_budgets table.
     * - Online → posts to server immediately; invalidates cache on success so
     *   next getBudgets() call re-fetches fresh data.
     */
    suspend fun setBudgetOfflineFirst(request: SetBudgetRequest): OperationResult {
        if (!networkMonitor.isOnline()) {
            pendingBudgetDao.insert(request.toPendingEntity())
            return OperationResult.Queued
        }
        return when (val res = utils.safeApiCall { budgetApi.setBudget(request) }) {
            is UiState.Success -> {
                sessionCacheManager.invalidate(SessionCacheManager.budgetKey(request.month))
                OperationResult.Synced
            }
            is UiState.Error -> OperationResult.Failed(res.message)
            else             -> OperationResult.Failed("Unknown error")
        }
    }

    /**
     * Bulk-syncs all pending budget operations; falls back to one-by-one on error.
     */
    suspend fun syncPendingBudgets(): SyncResult {
        if (!networkMonitor.isOnline()) {
            return SyncResult(synced = 0, remaining = pendingBudgetDao.count())
        }
        val pending = pendingBudgetDao.getAll()
        if (pending.isEmpty()) return SyncResult(synced = 0, remaining = 0)

        val bulkResult = utils.safeApiCall {
            budgetApi.setBudgetsBulk(BulkBudgetSyncRequest(pending.map { it.toRequest() }))
        }
        if (bulkResult is UiState.Success) {
            pending.map { it.month }.distinct()
                .forEach { sessionCacheManager.invalidate(SessionCacheManager.budgetKey(it)) }
            pendingBudgetDao.clearAll()
            return SyncResult(synced = pending.size, remaining = 0)
        }

        // Bulk endpoint unavailable — fall back to one-by-one
        var synced = 0
        for (item in pending) {
            val res = utils.safeApiCall { budgetApi.setBudget(item.toRequest()) }
            if (res is UiState.Success) {
                sessionCacheManager.invalidate(SessionCacheManager.budgetKey(item.month))
                pendingBudgetDao.deleteById(item.localId)
                synced++
            } else break
        }
        return SyncResult(synced = synced, remaining = pendingBudgetDao.count())
    }

    // ─── WorkManager entry point ───────────────────────────────────────────────

    /** Called by SyncWorker to drain both pending queues in one pass. */
    suspend fun syncAllPending() {
        syncPendingExpenses()
        syncPendingBudgets()
    }
}

package com.example.expense.data.repository

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.example.expense.data.api.ApiInterface
import com.example.expense.data.api.BudgetInterface
import com.example.expense.data.api.ExpenseInterface
import com.example.expense.data.api.HomeApiInterface
import com.example.expense.core.util.SessionCacheManager
import com.example.expense.core.util.Utils
import com.example.expense.data.local.AddExpenseResult
import com.example.expense.data.local.BudgetDao
import com.example.expense.data.local.BudgetEntity
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
import com.example.expense.core.util.TokenManager
import com.example.expense.data.api.DataSyncApiInterface
import com.example.expense.data.local.ExpenseEntity
import com.example.expense.data.local.toDomain
import com.example.expense.data.local.toDomainList1
import com.example.expense.data.local.toEntity
import com.example.expense.data.local.toEntityListExpenses
import com.example.expense.data.local.toEntityListX
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.ApiResponse
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.BudgetSyncDataRequest
import com.example.expense.data.model.BudgetSynceDataResponse
import com.example.expense.data.model.BulkBudgetSyncRequest
import com.example.expense.data.model.BulkExpenseSyncRequest
import com.example.expense.data.model.ByCategory
import com.example.expense.data.model.ByMonth
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.ChangePasswordRequest
import com.example.expense.data.model.DeleteResponse
import com.example.expense.data.model.Expense
import com.example.expense.data.model.ExpenseDatSyncRequest
import com.example.expense.data.model.ExpenseQuery
import com.example.expense.data.model.ExpenseSummaryData
import com.example.expense.data.model.ExpenseSummaryResponse
import com.example.expense.data.model.ExpenseSynceDataResponse
import com.example.expense.data.model.ExpenseWeeklySummaryResponse
import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.ExpensesResponse
import com.example.expense.data.model.FcmTokenRequest
import com.example.expense.data.model.GetExpenseSyncDataResponse
import com.example.expense.data.model.GetSyncBudgetResponse
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Int

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
    private val sessionCacheManager: SessionCacheManager,
    private val tokenManager: TokenManager,
    private val dataSyncApiInterface: DataSyncApiInterface,
    private val firestore: FirebaseFirestore
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

    suspend fun sendFcmToken(token: String): UiState<ApiResponse<DeleteResponse>> =
        utils.safeApiCall { homeApi.sendFcmToken(FcmTokenRequest(token)) }

    // ─── Home / Summary (session-cached GET) ──────────────────────────────────

    /**
     * Returns session-cached summary first; only hits the network if this
     * month's data hasn't been fetched yet in the current app session.
     * Pass forceRefresh = true to bypass the cache (e.g. pull-to-refresh).
     */
//    suspend fun getSummary(month: String?, forceRefresh: Boolean = false): UiState<ExpenseSummaryResponse> {
//        val key = SessionCacheManager.homeSummaryKey(month)
//        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
//            val json = sessionCacheManager.getString(key)
//            if (json != null) {
//                val cached = gson.fromJson(json, ExpenseSummaryResponse::class.java)
//                return UiState.Success(cached)
//            }
//        }
//        val result = utils.safeApiCall { homeApi.getSummary(month) }
//        if (result is UiState.Success) {
//            sessionCacheManager.putString(key, gson.toJson(result.data))
//        }
//        return result
//    }
    suspend fun getSummary(month: String?, forceRefresh: Boolean = false): UiState<ExpenseSummaryResponse> {

        Log.d("VVV",month.toString())

        return try {

            val expenses = expenseDao.getMonthlyExpenses(month)
            val budgets = budgetDao.getBudgets(month)
            var totalWeeklyExpenseAmount = 0


            for(expense in expenses.takeLast(6)){

            }

            Log.d("VVV",expenses.toString()+"List Expense ")

            val totalSpent = expenses.sumOf { it.amount.toInt() }
            val totalBudget = budgets.sumOf { it.limitAmount }



            Log.d("VVV", totalBudget.toString())



            val byCategory = expenses
                .groupBy { it.categoryId }
                .map { (_, list) ->

                    val amount = list.sumOf { it.amount.toInt() }
                    Log.d("VVV",amount.toString()+"cat amount")

                    ByCategory(
                        amount = amount,
                        categoryColor = list.first().categoryColor ,
                        categoryId = list.first().categoryId ,
                        categoryName = list.first().categoryName,
                        categoryIcon = list.first().categoryIcon,
                        percentage = if (totalSpent == 0) 0 else (amount * 100 / totalSpent)
                    )
                }

            val byMonth = if (month.isNullOrBlank()) {
                emptyList()
            } else {
                val fromMonth = shiftMonth(month, -5)
                val trendExpenses = expenseDao.getExpensesBetweenMonths(fromMonth, month)
                val amountByMonth = trendExpenses
                    .groupBy { it.date.substring(0, 7) } // yyyy-MM
                    .mapValues { (_, list) -> list.sumOf { it.amount.toInt() } }

                (0..5).map { offset ->
                    val bucketMonth = shiftMonth(fromMonth, offset)
                    ByMonth(month = bucketMonth, amount = amountByMonth[bucketMonth] ?: 0)
                }
            }

            UiState.Success(
                ExpenseSummaryResponse(
                    data = ExpenseSummaryData(
                        byCategory = byCategory,
                        byMonth = byMonth,
                        currency = "INR",
                        totalBudget = totalBudget,
                        totalSpent = totalSpent
                    ),

                    success = true
                )

            )

        } catch (e: Exception) {

            UiState.Error(e.message ?: "Something went wrong")
        }
    }

    /** Shifts a "yyyy-MM" month string by [delta] months (negative moves back). */
    private fun shiftMonth(month: String, delta: Int): String {
        val parts = month.split("-")
        val total = parts[0].toInt() * 12 + (parts[1].toInt() - 1) + delta
        val year = Math.floorDiv(total, 12)
        val mon = Math.floorMod(total, 12) + 1
        return "%04d-%02d".format(year, mon)
    }

    /**
     * Weekly summary - was fetched from an authenticated REST endpoint, which always fails
     * offline (see CLAUDE.md "Offline mode") and left Home's "This Week" box stuck on its
     * hardcoded ₹3000 placeholder forever since the failed call just left weekSpent
     * unchanged. Rewritten below to compute the last 7 days' spend from Room instead,
     * the same offline-first approach getSummary() already uses.
     */
//    suspend fun getWeeklySummary(forceRefresh: Boolean = false): UiState<ApiResponse<ExpenseWeeklySummaryResponse>> {
//        val key = SessionCacheManager.KEY_WEEKLY_SUMMARY
//        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
//            val json = sessionCacheManager.getString(key)
//            if (json != null) {
//                val type = object : TypeToken<ApiResponse<ExpenseWeeklySummaryResponse>>() {}.type
//                val cached = gson.fromJson<ApiResponse<ExpenseWeeklySummaryResponse>>(json, type)
//                return UiState.Success(cached)
//            }
//        }
//        val result = utils.safeApiCall { homeApi.getWeeklySummary() }
//        if (result is UiState.Success) {
//            sessionCacheManager.putString(key, gson.toJson(result.data))
//        }
//        return result
//    }
    suspend fun getWeeklySummary(forceRefresh: Boolean = false): UiState<ApiResponse<ExpenseWeeklySummaryResponse>> {
        return try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val calendar = java.util.Calendar.getInstance()
            val toDate = dateFormat.format(calendar.time)
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -6)
            val fromDate = dateFormat.format(calendar.time)

            val weekExpenses = expenseDao.getExpensesBetweenDates(fromDate, toDate)
            val totalSpent = weekExpenses.sumOf { it.amount.toIntOrNull() ?: 0 }

            UiState.Success(
                ApiResponse(
                    success = true,
                    data = ExpenseWeeklySummaryResponse(from = fromDate, to = toDate, totalSpent = totalSpent)
                )
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Something went wrong")
        }
    }

    suspend fun getSyncForExpense(): UiState<ApiResponse<GetExpenseSyncDataResponse>>{
       val result = utils.safeApiCall { dataSyncApiInterface.getSyncForExpense()}

        if(result is UiState.Success){
            saveExpensesLocally(result.data.data.expenses.toEntityListExpenses())
        }
        return result

    }

    suspend fun saveExpensesLocally(expenses: List<ExpenseEntity>){
        expenseDao.insertBulkExpenses(expenses)

    }

    suspend fun getSyncForBudget(): UiState<ApiResponse<GetSyncBudgetResponse>> {
        val result = utils.safeApiCall { dataSyncApiInterface.getSyncForBudget() }
        if (result is UiState.Success) {
            saveBudgetsLocally(result.data.data.budgets.toEntityListX())
        }
        return result
    }

    suspend fun saveBudgetsLocally(budgets: List<BudgetEntity>) {
        budgetDao.upsertBudgets(budgets)
    }



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

    /** Reads a single expense straight from the local Room cache. */
    suspend fun getExpenseDetail(id: String): UiState<ApiResponse<ExpenseX>> {
        return try {
            val entity = expenseDao.getExpenseById(id)
            if (entity != null) {
                UiState.Success(ApiResponse(success = true, data = entity.toDomain()))
            } else {
                UiState.Error("Expense not found")
            }
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Something went wrong")
        }
    }

    suspend fun updateExpense(id: String, request: UpdateExpenseRequest): UiState<ApiResponse<ExpenseX>> =
        utils.safeApiCall { expenseApi.updateExpense(id, request) }

    /** Updates an existing expense in place in Room, keeping its id/createdAt. */
    suspend fun updateExpenseLocal(id: String, request: AddExpenseRequest): UiState<String> {
        return try {
            val existing = expenseDao.getExpenseById(id)
                ?: return UiState.Error("Expense not found")

            val categories = categoryDao.getCategoryById(request.categoryId)

            val updated = existing.copy(
                amount = request.amount,
                date = request.date,
                note = request.note,
                categoryId = request.categoryId,
                categoryName = categories?.name ?: existing.categoryName,
                categoryColor = categories?.color ?: existing.categoryColor,
                categoryIcon = categories?.icon ?: existing.categoryIcon
            )

            expenseDao.upsertExpense(updated)
            UiState.Success("Expense updated successfully")
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Something went wrong")
        }
    }

    /** Deletes an expense from the local Room cache. */
    suspend fun deleteExpense(id: String): UiState<ApiResponse<DeleteResponse>> {
        return try {
            expenseDao.deleteById(id)
            UiState.Success(ApiResponse(success = true, data = DeleteResponse("Expense deleted successfully")))
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Something went wrong")
        }
    }

    suspend fun addExpense(request: AddExpenseRequest): UiState<ApiResponse<AddExpenseResponse>> =
        utils.safeApiCall { expenseApi.addExpense(request) }



    suspend fun addExpenseLocal(
        request: AddExpenseRequest
    ): UiState<String> {


        return try {

            val categories = categoryDao.getCategoryById(request.categoryId)



            val updatedRequest = request.copy(

                categoryName = categories?.name ?: "Bills",
                categoryColor = categories?.color ?: "#FFEAA7",
                categoryIcon = categories?.icon ?:"ic_bills",
                currency = "INR",
                userId = tokenManager.getUserId() ?: ""
            )


            expenseDao.insertExpense(
                updatedRequest.toEntity()
            )

            UiState.Success("Expense Added Successfully")

        } catch (e: Exception) {

            UiState.Error(
                e.message ?: "Something went wrong"
            )
        }
    }

//    suspend fun getExpensesSearch(query: ExpenseQuery): UiState<ApiResponse<SearchApiData>> =
//        utils.safeApiCall {
//            expenseApi.getExpenses(
//                page = query.page,
//                limit = query.limit,
//                category = query.category,
//                from = query.from,
//                to = query.to,
//                search = query.search
//            )
//        }

    // ─── Room: local expense cache ─────────────────────────────────────────────

    fun observeCachedExpenses(): Flow<List<ExpenseX>> =
        expenseDao.observeExpenses().map { it.toDomainList() }

    suspend fun cacheExpenses(expenses: List<ExpenseX>) =
        expenseDao.upsertExpenses(expenses.toEntityList())

    suspend fun clearExpenseCache() = expenseDao.clearAll()

    /** For Settings > Export Data - the month's expenses (yyyy-MM) as flat entities, ready
     * for ExpenseCsvExporter without going through the ExpenseX domain mapping. */
    suspend fun getExpensesForExportMonth(month: String): List<ExpenseEntity> =
        expenseDao.getMonthlyExpenses(month)

    /** For Settings > Export Data's custom-range option - both dates inclusive, yyyy-MM-dd. */
    suspend fun getExpensesForExportRange(fromDate: String, toDate: String): List<ExpenseEntity> =
        expenseDao.getExpensesBetweenDates(fromDate, toDate)

//    suspend fun refreshExpenses(query: ExpenseQuery): UiState<ApiResponse<SearchApiData>> {
//        val result = getExpensesSearch(query)
//        if (result is UiState.Success) cacheExpenses(result.data.data.expenses)
//        return result
//    }

    // ─── Offline expense add + bulk sync ──────────────────────────────────────

//    fun observePendingExpenseCount(): Flow<Int> = pendingExpenseDao.observeCount()
//
//    suspend fun addExpenseOfflineFirst(request: AddExpenseRequest): AddExpenseResult {
//        if (!networkMonitor.isOnline()) {
//            pendingExpenseDao.insert(request.toPendingEntity())
//            return AddExpenseResult.Queued
//        }
//        return when (val res = addExpense(request)) {
//            is UiState.Success -> AddExpenseResult.Synced(res.data.data)
//            is UiState.Error   -> AddExpenseResult.Failed(res.message)
//            else               -> AddExpenseResult.Failed("Unknown error")
//        }
//    }

    /**
     * Bulk-syncs all pending expenses in one request; falls back to one-by-one
     * if the bulk endpoint is unavailable or returns an error.
     */
//    suspend fun syncPendingExpenses(): SyncResult {
//        if (!networkMonitor.isOnline()) {
//            return SyncResult(synced = 0, remaining = pendingExpenseDao.count())
//        }
//        val pending = pendingExpenseDao.getAll()
//        if (pending.isEmpty()) return SyncResult(synced = 0, remaining = 0)
//
//        val bulkResult = utils.safeApiCall {
//            expenseApi.addExpensesBulk(BulkExpenseSyncRequest(pending.map { it.toRequest() }))
//        }
//        if (bulkResult is UiState.Success) {
//            pendingExpenseDao.clearAll()
//            return SyncResult(synced = pending.size, remaining = 0)
//        }
//
//        // Bulk endpoint unavailable — fall back to one-by-one FIFO
//        var synced = 0
//        for (item in pending) {
//            val res = addExpense(item.toRequest())
//            if (res is UiState.Success) {
//                pendingExpenseDao.deleteById(item.localId)
//                synced++
//            } else break
//        }
//        return SyncResult(synced = synced, remaining = pendingExpenseDao.count())
//    }

    // ─── Categories (session-cached GET, stored in Room) ──────────────────────

    /**
     * Serves categories from Room. Only fetches from Firestore once per session.
     *
     * This used to call the categories REST endpoint (`expenseApi.getExpenseCat()`), but
     * that requires a login token - which isn't available while the app runs fully offline
     * (see CLAUDE.md "Offline mode"), and calling it with none crashed screens that assumed
     * categories would always load. Categories are default/global (not user-specific), so
     * they're served from a public "categories" collection in Firestore instead - no auth
     * needed. The old endpoint call is left in ExpenseInterface, just unused.
     */
    suspend fun getExpenseCat(forceRefresh: Boolean = false): UiState<ApiResponse<List<CatDataResponse>>> {


        val key = SessionCacheManager.KEY_CATEGORIES
        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
            val cached = categoryDao.getAll()
            if (cached.isNotEmpty()) {
                return UiState.Success(ApiResponse(success = true, data = cached.toDomainList()))
            }
        }
        // val result = utils.safeApiCall { expenseApi.getExpenseCat() }
        val result: UiState<ApiResponse<List<CatDataResponse>>> = try {
            val snapshot = firestore.collection("categories").get().await()
            val categories = snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                CatDataResponse(
                    id = doc.getString("id") ?: doc.id,
                    name = name,
                    color = doc.getString("color") ?: "#B0BEC5",
                    icon = doc.getString("icon") ?: "ic_other",
                    isDefault = doc.getBoolean("isDefault") ?: false
                )
            }
            UiState.Success(ApiResponse(success = true, data = categories))
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Failed to load categories")
        }
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
        return try {

            val result = budgetDao.getBudgets(month)

            val response = ApiResponse(
                success = true,
                data = result.map { it.toDomain() }
            )
            Log.d("Budget",response.toString())

            UiState.Success(response)

        }catch (e: Exception){
            UiState.Error(e.message?: "Something wrong")
        }
//        val key = SessionCacheManager.budgetKey(month)
//        if (!forceRefresh && sessionCacheManager.isFetched(key)) {
//            val cached = budgetDao.getBudgets(month ?: "")
//            if (cached.isNotEmpty()) {
//                return UiState.Success(ApiResponse(success = true, data = cached.toDomainList()))
//            }
//        }
//        val result = utils.safeApiCall { budgetApi.getBudgets(month) }
//        if (result is UiState.Success) {
//            if (!month.isNullOrEmpty()) budgetDao.clearForMonth(month)
//            else budgetDao.clearAll()
//            budgetDao.upsertBudgets(result.data.data.toEntityList())
//            sessionCacheManager.markFetched(key)
//        }
//        return result
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
    suspend fun setBudgetOfflineFirst(request: SetBudgetRequest): UiState<String> {
        return try {
            val categories = categoryDao.getCategoryById(request.categoryId)
            val expenseById = expenseDao.getExpensesById(request.categoryId)


            val updateRequest = request.copy(
                categoryName = categories?.name ?: "name",
                categoryColor = categories?.color ?: "color",
                spendAmount = expenseById.sumOf { it.amount.toInt() },
                categoryIcon = categories?.icon ?: "icon"


            )
            Log.d("Budget", updateRequest.toString())
            budgetDao.insertBudget(updateRequest.toEntity())
            UiState.Success("Expense Added Successfully")
        }catch (e: Exception){
            UiState.Error(e.message ?: "Something wrong")
        }

    }

    /** Removes a single budget from the local Room cache. */
    suspend fun deleteBudget(categoryId: String, month: String): UiState<String> {
        return try {
            budgetDao.deleteBudget(categoryId, month)
            UiState.Success("Budget deleted")
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Something wrong")
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
//    suspend fun syncAllPending() {
//        syncPendingExpenses()
//        syncPendingBudgets()
//    }
    suspend fun syncData(): UiState<Unit> {

        val budgets = budgetDao.getAllBudget()
        if (budgets.isNotEmpty()) {
            val budgetResult = utils.safeApiCall {
                dataSyncApiInterface.syncBudget(
                    BudgetSyncDataRequest(budgets.toDomainList1())
                )
            }

            if (budgetResult is UiState.Error) {
                return UiState.Error(budgetResult.message)
            }
        }

        val expenses = expenseDao.getAllExpenses()
        if (expenses.isNotEmpty()) {
            val expenseResult = utils.safeApiCall {
                dataSyncApiInterface.syncExpense(
                    ExpenseDatSyncRequest(expenses.toDomainList1())
                )
            }

            if (expenseResult is UiState.Error) {
                return UiState.Error(expenseResult.message)
            }
        }

        return UiState.Success(Unit)
    }


    suspend fun ClearAllLocalData(){
        expenseDao.clearAll()
        budgetDao.clearAll()
    }
}

package com.example.data.model

import com.example.ApiInterface
import com.example.BudgetInterface
import com.example.ExpenseInterface
import com.example.HomeApiInterface
import com.example.Utlity.Utils
import com.example.data.Api.SearchApiData
import com.example.data.model.SetBudgetRequest
import com.example.data.UiState
import com.example.data.model.User
import com.example.data.model.setBudgetResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api : ApiInterface,
                                     private val homeApi : HomeApiInterface,
                                     private val budgetApi: BudgetInterface
                                     , private val expenseApi : ExpenseInterface
) {

    @Inject
    lateinit var utils: Utils


    suspend fun registerUser(registerUserRequest: RegisterUserRequest): UiState<RegisterUserResponse> {
      return utils.safeApiCall { api.registerUser(registerUserRequest) }
    }

    suspend fun loginUser(loginUserRequest: LoginUserRequest): UiState<LoginUserResponse> {
        return utils.safeApiCall { api.loginUser(loginUserRequest) }
    }

    suspend fun getSummary(month: String?): UiState<ExpenseSummaryResponse> {
        return utils.safeApiCall { homeApi.getSummary(month) }
    }

    suspend fun getWeeklySummary(): UiState<ApiResponse<ExpenseWeeklySummaryResponse>> {
        return utils.safeApiCall { homeApi.getWeeklySummary() }
    }

    suspend fun getExpenses(page: Int , limit : Int): UiState<ExpensesResponse> {
        return utils.safeApiCall {homeApi.getExpenses(page = page,limit=limit)  }
    }

    suspend fun getUserProfileDetail(): UiState<ApiResponse<User>> {
        return utils.safeApiCall { homeApi.getProfileDetail() }
    }


    suspend fun getExpenseCat(): UiState<ApiResponse<List<CatDataResponse>>> {
        return utils.safeApiCall { expenseApi.getExpenseCat() }
    }


    suspend fun addExpense(addExpenseRequest: AddExpenseRequest): UiState<ApiResponse<AddExpenseResponse>> {
        return utils.safeApiCall { expenseApi.addExpense(addExpenseRequest) }
    }

    suspend fun getExpensesSearch(expenseQuery: ExpenseQuery): UiState<ApiResponse<SearchApiData>> {
        return utils.safeApiCall { expenseApi.getExpenses(
            page = expenseQuery.page,
            limit = expenseQuery.limit,
            category = expenseQuery.category,
            from = expenseQuery.from,
            to = expenseQuery.to,
            search = expenseQuery.search
        ) }
    }

    suspend fun getBudgets(month: String?): UiState<ApiResponse<List<BudgetData>>> {
        return utils.safeApiCall { budgetApi.getBudgets(month) }
    }

    suspend fun setBudget(setBudgetRequest: SetBudgetRequest): UiState<ApiResponse<setBudgetResponse>> {
        return utils.safeApiCall { budgetApi.setBudget(setBudgetRequest) }
    }




}
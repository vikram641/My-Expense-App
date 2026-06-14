package com.example.expense.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.expense.ExpenseApp
import com.example.expense.data.api.ApiInterface
import com.example.expense.data.api.BudgetInterface
import com.example.expense.data.api.ExpenseInterface
import com.example.expense.data.api.HomeApiInterface
import com.example.expense.core.network.AuthInterceptor
import com.example.expense.core.util.AppENUM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private fun getBaseUrl(): String {
        return ExpenseApp.applicationContext()
            .getSharedPreferences(
                AppENUM.RefactoredStrings.APP_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                AppENUM.RefactoredStrings.BASE_URL,
                "https://expense-app-backend-91yj.onrender.com/"
            ) ?: "https://expense-app-backend-91yj.onrender.com/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            // ✅ TIMEOUTS (FIX FOR RENDER COLD START)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)

            // ✅ Retry if connection fails
            .retryOnConnectionFailure(true)

            // ✅ Auth Header
            .addInterceptor(authInterceptor)

            // ✅ Chucker for debugging
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250_000L)
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())

    }


    @Provides
    @Singleton
    fun authApI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): ApiInterface {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun otherApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): HomeApiInterface {

        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(HomeApiInterface::class.java)


    }
    @Singleton
    @Provides
    fun expenseApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): ExpenseInterface {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(ExpenseInterface::class.java)
    }
    @Singleton
    @Provides
    fun budgetApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): BudgetInterface {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(BudgetInterface::class.java)
    }

}
package com.example.data.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ApiInterface
import com.example.BudgetInterface
import com.example.ExpenseInterface
import com.example.HomeApiInterface
import com.example.Utlity.AuthInterceptor
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
class AppModule {

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
            .baseUrl("http://172.16.19.112:3000")
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
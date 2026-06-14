package com.example.expense.di

import android.content.Context
import androidx.room.Room
import com.example.expense.data.local.BudgetDao
import com.example.expense.data.local.CategoryDao
import com.example.expense.data.local.ExpenseDao
import com.example.expense.data.local.ExpenseDatabase
import com.example.expense.data.local.PendingBudgetDao
import com.example.expense.data.local.PendingExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase =
        Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun providePendingExpenseDao(db: ExpenseDatabase): PendingExpenseDao =
        db.pendingExpenseDao()

    @Provides
    @Singleton
    fun provideBudgetDao(db: ExpenseDatabase): BudgetDao = db.budgetDao()

    @Provides
    @Singleton
    fun providePendingBudgetDao(db: ExpenseDatabase): PendingBudgetDao =
        db.pendingBudgetDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: ExpenseDatabase): CategoryDao = db.categoryDao()
}

package com.example.expense.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.expense.core.UiState
import com.example.expense.data.local.toEntityListX
import com.example.expense.data.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            val syncType = inputData.getString(KEY_SYNC_TYPE)
            when(syncType){
                LOGOUT_SYNC->{
                    val result= repository.syncData()
                    if (result is UiState.Success) {
                        repository.ClearAllLocalData()
                    }


                }
                LOGIN_SYNC->{
                    repository.getSyncForExpense()
                    repository.getSyncForBudget()
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    companion object {
        private const val PERIODIC_WORK_NAME = "expense_sync_periodic"
        private const val ONE_TIME_WORK_NAME = "expense_sync_one_time"
        private const val KEY_SYNC_TYPE = "sync_type"


        const val LOGIN_SYNC = "login"
        const val LOGOUT_SYNC = "logout"


        private val networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /** Enqueues a periodic sync every 15 minutes whenever the device is online. */
        fun enqueuePeriodic(context: Context) {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(networkConstraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        /** Enqueues a one-time sync that runs as soon as the device is online. */
        fun enqueueOneTime(context: Context, syncType: String) {
            val inputData: Data = workDataOf(
                KEY_SYNC_TYPE to syncType
            )

            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                ONE_TIME_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                request
            )
        }
    }
}

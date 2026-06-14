package com.example.expense

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.expense.core.util.SessionCacheManager
import com.example.expense.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExpenseApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var sessionCacheManager: SessionCacheManager
    // Companion object static access ke liye
    companion object {
        private var instance: ExpenseApp? = null

        /**
         * Is function ko aap pure project mein kahin se bhi call karke
         * Bina parameters ke direct Application Context le sakte hain.
         */
        fun applicationContext(): Context {
            return instance?.applicationContext
                ?: throw IllegalStateException("ExpenseApp is not initialized yet.")
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Clear all session cache flags so data is re-fetched on each app launch
        sessionCacheManager.startNewSession()
        // Schedule periodic background sync + an immediate one-time sync
        SyncWorker.enqueuePeriodic(this)
        SyncWorker.enqueueOneTime(this)
    }
}

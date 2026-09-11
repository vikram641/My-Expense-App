package com.example.expense

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.expense.core.util.SessionCacheManager
import com.example.expense.sync.SyncWorker
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
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
//        SyncWorker.enqueuePeriodic(this)
//        SyncWorker.enqueueOneTime(this)

        MobileAds.initialize(this) { /* init status callback, safe to ignore */ }
        if (BuildConfig.DEBUG) {
            // Registers the OnePlus Nord dev device this app was verified on
            // (2026-09-11) as a test device. A different physical device will log its
            // own hashed ID on first run ("Use RequestConfiguration.Builder()...") -
            // add it here too rather than replacing this one.
            val config = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("5ECA94DDBA24462E82FE5C3DB1B8B82A"))
                .build()
            MobileAds.setRequestConfiguration(config)
        }
    }
}

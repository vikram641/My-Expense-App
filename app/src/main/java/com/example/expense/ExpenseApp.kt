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
            // Avoid triggering real ad requests from this dev device - see
            // Logcat on first run for "Use RequestConfiguration.Builder()..." with
            // this device's actual test-device ID and replace the placeholder below.
            val config = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("TEST-DEVICE-ID-FROM-LOGCAT"))
                .build()
            MobileAds.setRequestConfiguration(config)
        }
    }
}

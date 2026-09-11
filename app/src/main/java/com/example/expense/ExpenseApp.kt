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
        // Registers dev/test devices as AdMob test devices, as defense in depth -
        // applies in BOTH debug and release builds. NOT relied on as the sole
        // safeguard though: this OnePlus Nord's hashed test-device ID was observed to
        // change between sessions (2026-09-11 - likely OEM-specific Advertising ID
        // behavior), so a real interstitial served and rendered despite this being
        // registered with the *previous* hash. The actual safeguard against real ads
        // during local testing is AdManager.USE_REAL_AD_UNITS staying false - see
        // that constant's comment before ever flipping it. This has zero effect on
        // real end users' devices either way; only the hashed IDs listed below ever
        // get test ads. Add new devices' IDs here (logged on first run, see
        // "Use RequestConfiguration.Builder()...") rather than replacing old ones,
        // in case a hash like this one recurs.
        val config = RequestConfiguration.Builder()
            .setTestDeviceIds(
                listOf(
                    "5ECA94DDBA24462E82FE5C3DB1B8B82A", // OnePlus Nord dev device, seen 2026-09-11 (debug build)
                    "6268EABA190A8CCA3761DF1ED4AF0037"  // same physical device, different hash seen same day (release build)
                )
            )
            .build()
        MobileAds.setRequestConfiguration(config)
    }
}

package com.example.expense.core.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gates ad loading behind Google's UMP consent flow (required for EEA/UK traffic,
 * harmless elsewhere). [requestConsent] must be called once, early (MainActivity),
 * before any screen tries to load an ad - [AdManager] awaits [awaitReady] first.
 *
 * Resolves [awaitReady] either way (consent granted, declined, or the check itself
 * failed) - a broken consent SDK call must never permanently block ads from loading.
 */
@Singleton
class AdConsentManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val readyDeferred = CompletableDeferred<Unit>()

    fun requestConsent(activity: Activity) {
        val params = ConsentRequestParameters.Builder().build()
        val consentInfo = UserMessagingPlatform.getConsentInformation(context)

        consentInfo.requestConsentInfoUpdate(
            activity,
            params,
            {
                if (consentInfo.isConsentFormAvailable) {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                        // formError is non-null on a failed show/load, but there's
                        // nothing more useful to do than proceed either way.
                        completeReady()
                    }
                } else {
                    completeReady()
                }
            },
            {
                // requestConsentInfoUpdate itself failed (e.g. no network) - proceed
                // anyway rather than blocking ads forever.
                completeReady()
            }
        )
    }

    suspend fun awaitReady() = readyDeferred.await()

    private fun completeReady() {
        if (!readyDeferred.isCompleted) readyDeferred.complete(Unit)
    }
}

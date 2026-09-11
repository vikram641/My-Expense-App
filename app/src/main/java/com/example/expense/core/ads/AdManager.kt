package com.example.expense.core.ads

import android.app.Activity
import android.content.Context
import com.example.expense.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

// Real ad units, created in the AdMob console 2026-09-11 - each is shared across
// multiple placements (see docs/GOOGLE_ADS_ADMOB_INTEGRATION.md) rather than one
// unit per screen, since this app has no need for per-placement reporting yet.
private const val REAL_BANNER_AD_UNIT_ID = "ca-app-pub-7178541936408370/7875983914" // "Shared Banner (Home, Expense Detail, Profile)"
private const val REAL_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-7178541936408370/2157482326" // "Shared Interstitial (Export Data, SIP Calculator)"

@Singleton
class AdManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Test IDs in debug builds only - never test against real ad units on a personal
    // device (self-serves count as invalid traffic and can get the AdMob account suspended).
    private val bannerAdUnitId: String = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else REAL_BANNER_AD_UNIT_ID
    private val interstitialAdUnitId: String =
        if (BuildConfig.DEBUG) TEST_INTERSTITIAL_AD_UNIT_ID else REAL_INTERSTITIAL_AD_UNIT_ID

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false

    fun loadBannerAd(adView: AdView) {
        adView.adUnitId = bannerAdUnitId
        adView.loadAd(AdRequest.Builder().build())
    }

    /**
     * Shows a preloaded interstitial if one is ready, otherwise skips straight to
     * [onDismissed] - callers must never block navigation on ad availability. Always
     * kicks off loading the next interstitial so the one after is more likely ready.
     */
    fun showInterstitial(activity: Activity, onDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            preloadInterstitial()
            onDismissed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                preloadInterstitial()
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                preloadInterstitial()
                onDismissed()
            }
        }
        ad.show(activity)
    }

    /** Call once, early (e.g. app start or first screen that might show one), so an
     * interstitial is likely already loaded by the time [showInterstitial] is called. */
    fun preloadInterstitial() {
        if (isLoadingInterstitial || interstitialAd != null) return
        isLoadingInterstitial = true
        InterstitialAd.load(
            context,
            interstitialAdUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    isLoadingInterstitial = false
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    isLoadingInterstitial = false
                    interstitialAd = null
                }
            }
        )
    }
}

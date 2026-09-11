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

/**
 * Real ad unit IDs haven't been created in the AdMob console yet (only the app entry
 * has) - these are Google's public test units. Replace with real ones from AdMob
 * before shipping a release build; never ship these test IDs in `release`.
 */
private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

@Singleton
class AdManager @Inject constructor(@ApplicationContext private val context: Context) {

    // TODO: replace both with real ad unit IDs from the AdMob console before release.
    private val bannerAdUnitId: String = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else TEST_BANNER_AD_UNIT_ID
    private val interstitialAdUnitId: String =
        if (BuildConfig.DEBUG) TEST_INTERSTITIAL_AD_UNIT_ID else TEST_INTERSTITIAL_AD_UNIT_ID

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

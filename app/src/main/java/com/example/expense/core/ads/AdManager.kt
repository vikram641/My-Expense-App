package com.example.expense.core.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

// Real ad units, created in the AdMob console 2026-09-11 - each is shared across
// multiple placements (see docs/GOOGLE_ADS_ADMOB_INTEGRATION.md) rather than one
// unit per screen, since this app has no need for per-placement reporting yet.
private const val REAL_BANNER_AD_UNIT_ID = "ca-app-pub-7178541936408370/7875983914" // "Shared Banner (Home, Expense Detail, Profile)"
private const val REAL_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-7178541936408370/2157482326" // "Shared Interstitial (Export Data, SIP Calculator)"

/**
 * Deliberately NOT keyed off BuildConfig.DEBUG - a `release` build gets sideloaded
 * onto the developer's own device for testing too, and this device's AdMob
 * test-device hash was observed to change between sessions (`5ECA94DDBA...` then
 * `6268EABA...` on the same physical phone, 2026-09-11 - likely OEM-specific
 * Advertising ID behavior), so registering a test-device ID is not a reliable
 * safeguard here. A real interstitial (GoMechanic) served and was shown as a result.
 * Keep this `false` for all local/personal-device testing, regardless of build type.
 *
 * Flipped to `true` 2026-09-12 for the versionCode 9 release build (fixes the
 * XML-declared-AdView bug that showed a diagnostic error box instead of a real ad -
 * see AdMob integration memory). If you ever pull this exact build back down and
 * install it on a personal device to test something else, flip this back to `false`
 * first and rebuild - do not browse a personal device on a true-flagged build.
 */
private const val USE_REAL_AD_UNITS = true

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val consentManager: AdConsentManager
) {

    // See USE_REAL_AD_UNITS - never test against real ad units on a personal device
    // (self-serves count as invalid traffic and can get the AdMob account suspended).
    private val bannerAdUnitId: String = if (USE_REAL_AD_UNITS) REAL_BANNER_AD_UNIT_ID else TEST_BANNER_AD_UNIT_ID
    private val interstitialAdUnitId: String =
        if (USE_REAL_AD_UNITS) REAL_INTERSTITIAL_AD_UNIT_ID else TEST_INTERSTITIAL_AD_UNIT_ID

    // Lives as long as the app (this class is a Hilt singleton) - used only to let the
    // interstitial's own callbacks (onAdDismissed/onAdFailed) kick off the next
    // preload without needing a caller-supplied scope for that specific hand-off.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false

    /** Suspends until UMP consent has been gathered (or the check failed/wasn't
     * required) before actually requesting the ad - see [AdConsentManager].
     *
     * Takes a plain placeholder [container] (a `FrameLayout` in the XML, not an
     * `<AdView>` tag) and constructs the `AdView` itself, purely in code, via
     * `AdView(context)`. This is deliberate: an `<AdView>` declared in XML is built
     * through the SDK's `AdView(Context, AttributeSet)` constructor, which validates
     * *both* `adSize` and `adUnitId` as of play-services-ads 23.6.0 regardless of
     * what gets set programmatically afterward - since our ad unit ID must be chosen
     * at runtime (real vs. test, see [USE_REAL_AD_UNITS]), it can never be present in
     * XML, so that constructor path always flags one of the two as "missing" and
     * (on a registered test device) renders that diagnostic text as the ad creature
     * itself instead of a real ad. Constructing the `AdView` with the plain
     * `AdView(context)` constructor and setting both properties before adding it to
     * the container avoids that XML-attribute validation entirely - this is Google's
     * own documented pattern for runtime-determined ad units. Returns the created
     * `AdView` so the caller can drive its resume/pause/destroy lifecycle. */
    suspend fun loadBannerAd(container: ViewGroup): AdView {
        consentManager.awaitReady()
        val adView = AdView(container.context)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = bannerAdUnitId
        container.removeAllViews()
        container.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
        return adView
    }

    /**
     * Shows a preloaded interstitial if one is ready, otherwise skips straight to
     * [onDismissed] - callers must never block navigation on ad availability. Always
     * kicks off loading the next interstitial so the one after is more likely ready.
     * Synchronous (not gated on consent) since it only ever acts on whatever
     * [preloadInterstitial] already finished loading - there's nothing to await here.
     */
    fun showInterstitial(activity: Activity, onDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            scope.launch { preloadInterstitial() }
            onDismissed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                scope.launch { preloadInterstitial() }
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                scope.launch { preloadInterstitial() }
                onDismissed()
            }
        }
        ad.show(activity)
    }

    /** Call once, early (e.g. app start or first screen that might show one), so an
     * interstitial is likely already loaded by the time [showInterstitial] is called. */
    suspend fun preloadInterstitial() {
        if (isLoadingInterstitial || interstitialAd != null) return
        consentManager.awaitReady()
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

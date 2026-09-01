package com.example.expense

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.expense.core.util.OnboardingPrefs
import com.example.expense.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var onboardingPrefs: OnboardingPrefs

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val data = intent?.data
        Log.d("DeepLink", data.toString())
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // activity_main.xml has no static app:navGraph - building and assigning the graph
        // here, before it's ever set on the NavController, means splashFragment2 is never
        // instantiated at all (it would only be created if it were the start destination).
        // Onboarding (welcomeFragment onward) now lives in this same graph/Activity instead
        // of a separate OnboardingActivity - see "Splash screen" in CLAUDE.md for why a
        // second Activity meant a second, jarring system splash flash on first run.
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(
            if (onboardingPrefs.isOnboardingComplete()) R.id.dashboardFragment else R.id.welcomeFragment
        )
        navController.graph = navGraph

        // ✅ Dismiss instantly, no custom exit animation - nothing branded underneath to
        // slide-reveal into anymore, so an instant removal reads cleaner than animating.
        splashScreen.setKeepOnScreenCondition { false }
        splashScreen.setOnExitAnimationListener { splashProvider -> splashProvider.remove() }

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // SMS auto-detect disabled for Play Store release (see AndroidManifest.xml note)
        // if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        //     != PackageManager.PERMISSION_GRANTED) {
        //     ActivityCompat.requestPermissions(
        //         this,
        //         arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
        //         101
        //     )
        // }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                102
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent?.let {
            handleDeepLink(it)
        }
    }

    private fun handleDeepLink(intent: Intent) {

        val data: Uri? = intent.data

        data?.let { uri ->

            Log.d("DeepLink", uri.toString())

            val pathSegments = uri.pathSegments

            if (pathSegments.isNotEmpty()) {

                when (pathSegments[0]) {

                    "profile" -> {

                        val userId = pathSegments[1]

                        // open profile screen
                        Log.d("DeepLink", "UserId: $userId")

                    }

                    "product" -> {

                        val productId = pathSegments[1]

                        // open product screen
                    }
                }
            }
        }
    }
}
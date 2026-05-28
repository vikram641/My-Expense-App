package com.example.expense

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.expense.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val data = intent?.data
        Log.d("DeepLink", data.toString())
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
//        window.statusBarColor = android.graphics.Color.TRANSPARENT
//        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // ✅ Dismiss instantly — hand off to SplashFragment
        splashScreen.setKeepOnScreenCondition { false }

        // ✅ Purple screen slides up revealing SplashFragment underneath
        splashScreen.setOnExitAnimationListener { splashProvider ->
            val slideUp = ObjectAnimator.ofFloat(
                splashProvider.view,
                View.TRANSLATION_Y,
                0f,
                -splashProvider.view.height.toFloat()
            )
            slideUp.duration = 300
            slideUp.interpolator = AccelerateInterpolator()
            slideUp.doOnEnd { splashProvider.remove() }
            slideUp.start()
        }

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // onCreate mein
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                101
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
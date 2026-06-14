package com.example.expense.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.expense.BuildConfig
import com.example.expense.R
import com.example.expense.core.util.AppENUM



import java.util.Arrays

class ApiBaseUrlActivity : AppCompatActivity() {
    private lateinit var apiUrlSpinner: Spinner
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG) {
            // Close the activity immediately if not in debug mode
            Log.d("DEBUG_CHECK", BuildConfig.DEBUG.toString())
            finish()
            return
        }

        setContentView(R.layout.activity_environment)
        apiUrlSpinner = findViewById<Spinner>(R.id.spinner_api_urls)
        val saveButton = findViewById<Button>(R.id.btn_save_url)

        // Example API URLs
        val apiUrls = arrayOf(
            "https://expense-app-backend-91yj.onrender.com/",
            "http://192.168.0.116:3000/",
            "https://venal-kiesha-unwon.ngrok-free.dev/",
        )

        // Initialize Spinner
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, apiUrls
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        apiUrlSpinner.adapter = adapter

        val savedUrl = getSharedPreferences(AppENUM.RefactoredStrings.APP_NAME, MODE_PRIVATE)?.getString(AppENUM.RefactoredStrings.BASE_URL, apiUrls[0])

        // Set previously saved URL as selected
        val savedIndex = Arrays.asList(*apiUrls).indexOf(savedUrl)
        if (savedIndex >= 0) {
            apiUrlSpinner.setSelection(savedIndex)
        }
        saveButton.setOnClickListener { v: View? ->
            val selectedUrl = apiUrlSpinner.selectedItem.toString()
            saveBaseUrl(selectedUrl)
        }
    }

    private fun saveBaseUrl(url: String) {
        this.getSharedPreferences(AppENUM.RefactoredStrings.APP_NAME, MODE_PRIVATE)
            ?.edit()
            ?.putString(AppENUM.RefactoredStrings.BASE_URL, url)
            ?.commit()

        Log.d("yyyy", "Saved URL - "+url)

        Toast.makeText(this, "Base URL saved: $url", Toast.LENGTH_SHORT).show()
        restartApp()
    }

    private fun restartApp() {
        // Get the package manager and the launch intent for the app
        val intent: Intent? = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)

        intent?.let {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this, it, null)
        }

        // Kill the current process to ensure the app is restarted
        Process.killProcess(Process.myPid())
    }

}


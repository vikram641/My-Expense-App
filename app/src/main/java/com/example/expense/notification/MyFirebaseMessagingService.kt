package com.example.expense.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.expense.core.util.TokenManager
import com.example.expense.data.repository.Repository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FcmService"
private const val CHANNEL_ID = "expense_push_channel"

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var tokenManager: TokenManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token received")
        sendTokenToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("gggjjj", message.data.toString())

        val title = message.notification?.title ?: message.data["title"] ?: "Expense App"
        val body = message.notification?.body ?: message.data["body"] ?: ""

        showNotification(title, body)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    /**
     * Only hits the network if the user is logged in and this token hasn't already been sent.
     *
     * The actual send-to-server call is disabled while the app runs fully offline - see
     * CLAUDE.md "Offline mode (temporary)". The token is still cached locally so it's ready
     * to send once syncing/FCM is turned back on.
     */
    private fun sendTokenToServer(token: String) {
        tokenManager.saveFcmToken(token)

//        if (tokenManager.getToken().isNullOrEmpty()) {
//            tokenManager.saveFcmToken(token)
//            return
//        }
//        if (tokenManager.getFcmToken() == token) return
//
//        serviceScope.launch {
//            when (val result = repository.sendFcmToken(token)) {
//                is com.example.expense.core.UiState.Success -> tokenManager.saveFcmToken(token)
//                is com.example.expense.core.UiState.Error -> Log.e(TAG, "Failed to send FCM token: ${result.message}")
//                else -> {}
//            }
//        }
    }

    private fun showNotification(title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expense Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Sync and account notifications from Expense App"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val contentIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

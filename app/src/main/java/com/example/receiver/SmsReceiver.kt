package com.example.expense.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.expense.data.repository.Repository
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.R
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SmsReceiverEntryPoint {
        fun repository(): Repository
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SmsReceiver", "✅ Receiver triggered!")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            for (sms in messages) {
                val sender = sms.originatingAddress ?: ""
                val body = sms.messageBody ?: ""
                Log.d("SmsReceiver", "Sender: $sender")  // add karo
                Log.d("SmsReceiver", "Body: $body")       // add karo

                if (isBankMessage(sender, body)) {
                    Log.d("SmsReceiver", "✅ Bank message detected!")  // add karo

                    val amount = extractAmount(body)
                    val merchant = extractMerchant(body)
                    Log.d("SmsReceiver", "Amount: $amount")  // add karo


                    if (amount != null) {
                        addExpense(context, amount, merchant)
                    }
                }
            }
        }
    }

    private fun isBankMessage(sender: String, body: String): Boolean {
        val debitKeywords = listOf(
            "debited", "debit", "spent", "paid", "withdrawn",
            "sent rs", "sent rs."  // Kotak ka format ✅
        )
        return debitKeywords.any { body.lowercase().contains(it) }
    }

    private fun extractAmount(body: String): Int? {
        val patterns = listOf(
            // Kotak format: "Sent Rs.20.00" ✅
            Regex("""[Ss]ent\s+Rs\.([0-9,]+)"""),
            // General formats
            Regex("""(?:Rs\.?|INR|₹)\s*([0-9,]+)""", RegexOption.IGNORE_CASE),
            Regex("""([0-9,]+)\s*(?:Rs\.?|INR|₹)""", RegexOption.IGNORE_CASE),
            Regex("""debited.*?([0-9,]+)""", RegexOption.IGNORE_CASE)
        )
        for (pattern in patterns) {
            val match = pattern.find(body)
            if (match != null) {
                return match.groupValues[1].replace(",", "").toDoubleOrNull()?.toInt()
            }
        }
        return null
    }

    private fun extractMerchant(body: String): String {
        val patterns = listOf(
            // Kotak format: "Sent Rs.20.00 from Kotak Bank AC XXXX to [MERCHANT]"
            Regex("""to\s+([A-Za-z0-9\s@._]+?)(?:\s+on|\s+via|\.|,|$)""", RegexOption.IGNORE_CASE),
            Regex("""(?:at|@)\s+([A-Za-z0-9\s]+?)(?:\s+on|\s+via|\.|,|$)""", RegexOption.IGNORE_CASE),
            Regex("""VPA\s+([^\s]+)""", RegexOption.IGNORE_CASE)
        )
        for (pattern in patterns) {
            val match = pattern.find(body)
            if (match != null) return match.groupValues[1].trim()
        }
        return "Bank Transaction"
    }

    private fun addExpense(context: Context, amount: Int, merchant: String) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            SmsReceiverEntryPoint::class.java
        )
        val repository = entryPoint.repository()

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val request = AddExpenseRequest(
            amount = amount,
            categoryId = "cat_e6ee396f-d4a1-48cf-bee1-783e101e8524",       // default empty — user baad mein edit kar sakta hai
            currency = "INR",
            date = currentDate,
            note = merchant,
            receiptUrl = ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.addExpense(request)
            // Success hone par notification dikhao
            // Check karo switch on hai ya nahi
            val isNotificationEnabled = context
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean("sms_notification", true)

            if (isNotificationEnabled) {
                showNotification(
                    context = context,
                    amount = amount,
                    merchant = "₹$amount - $merchant added successfully"
                )
            }


        }
    }

    private fun showNotification(context: Context, amount: Int, merchant: String) {
        val channelId = "expense_sms_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // App open intent
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // "View Expense" button intent
        val viewIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val viewPendingIntent = PendingIntent.getActivity(
            context, 1, viewIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // "Dismiss" button intent
        val dismissIntent = Intent("DISMISS_NOTIFICATION")
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context, 2, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Auto Expense Tracker",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "SMS se automatic expense add hone par notification"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("💸 Expense Added — $merchant")
            .setContentText("₹$amount has been added to your expenses automatically.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("₹$amount has been added to your expenses automatically from $merchant.")
                    .setBigContentTitle("💸 Expense Added — $merchant")
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                dismissPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_view,
                "View Expense",
                viewPendingIntent
            )
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
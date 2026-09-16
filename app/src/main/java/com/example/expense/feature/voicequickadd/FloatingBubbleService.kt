package com.example.expense.feature.voicequickadd

import android.animation.ValueAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.expense.MainActivity
import com.example.expense.R
import com.example.expense.core.UiState
import com.example.expense.data.local.CategoryDao
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

private const val TAG = "FloatingBubbleService"
private const val NOTIFICATION_CHANNEL_ID = "voice_quickadd_channel"
private const val NOTIFICATION_ID = 4201

/**
 * A persistent draggable overlay bubble (the "like Grammarly" ask) - tap it anywhere
 * (home screen or any other app) to speak an expense; it's parsed by Gemini and saved
 * straight away, no extra screen in between. Runs as a foreground service purely so the
 * overlay window + mic access survive while FixMoney itself isn't in the foreground.
 */
@AndroidEntryPoint
class FloatingBubbleService : Service() {

    @Inject lateinit var repository: Repository
    @Inject lateinit var categoryDao: CategoryDao

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private lateinit var windowManager: WindowManager
    private lateinit var bubbleView: View
    private lateinit var bubbleBackground: ImageView
    private lateinit var bubbleIcon: ImageView
    private lateinit var bubbleLottie: LottieAnimationView
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var idleDrawable: GradientDrawable
    private lateinit var listeningDrawable: GradientDrawable

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var snapAnimator: ValueAnimator? = null

    // Drag-vs-tap detection
    private var downRawX = 0f
    private var downRawY = 0f
    private var downParamX = 0
    private var downParamY = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }
        addBubble()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun buildNotification(): Notification {
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Floating voice quick-add",
                NotificationManager.IMPORTANCE_MIN
            )
            manager.createNotificationChannel(channel)
        }

        val openApp = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.voice_bubble_notification_title))
            .setContentText(getString(R.string.voice_bubble_notification_text))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setContentIntent(openApp)
            .build()
    }

    private fun addBubble() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        bubbleView = LayoutInflater.from(this).inflate(R.layout.overlay_voice_bubble, null)
        bubbleBackground = bubbleView.findViewById(R.id.bubbleBackground)
        bubbleIcon = bubbleView.findViewById(R.id.bubbleIcon)
        bubbleLottie = bubbleView.findViewById(R.id.bubbleLottie)

        // Built in code, not from a <shape> XML resource - a shape drawable with no
        // explicit <size> has an undefined intrinsic size, which silently failed to
        // render at all (neither as android:background nor android:src) inside this
        // TYPE_APPLICATION_OVERLAY window on this device. An explicit setSize() sidesteps
        // that entirely.
        val bubbleSizePx = (58 * resources.displayMetrics.density).toInt()
        idleDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(getColor(R.color.accent))
            setSize(bubbleSizePx, bubbleSizePx)
        }
        listeningDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(getColor(R.color.expense_red))
            setSize(bubbleSizePx, bubbleSizePx)
        }
        bubbleBackground.setImageDrawable(idleDrawable)

        val overlayType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 300
        }

        bubbleView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downRawX = event.rawX
                    downRawY = event.rawY
                    downParamX = layoutParams.x
                    downParamY = layoutParams.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - downRawX).toInt()
                    val dy = (event.rawY - downRawY).toInt()
                    layoutParams.x = downParamX + dx
                    layoutParams.y = downParamY + dy
                    runCatching { windowManager.updateViewLayout(bubbleView, layoutParams) }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val moved = abs(event.rawX - downRawX) + abs(event.rawY - downRawY)
                    if (moved < 20f) onBubbleTapped() else snapToNearestEdge()
                    true
                }
                else -> false
            }
        }

        runCatching { windowManager.addView(bubbleView, layoutParams) }
            .onFailure { Log.w(TAG, "failed to add bubble overlay", it) }
    }

    /** After a drag, animates the bubble to rest against whichever screen edge it's closer to. */
    private fun snapToNearestEdge() {
        val screenWidth = resources.displayMetrics.widthPixels
        val bubbleWidthPx = (58 * resources.displayMetrics.density).toInt()
        val bubbleCenterX = layoutParams.x + bubbleWidthPx / 2
        val targetX = if (bubbleCenterX < screenWidth / 2) 0 else screenWidth - bubbleWidthPx

        snapAnimator?.cancel()
        snapAnimator = ValueAnimator.ofInt(layoutParams.x, targetX).apply {
            duration = 250
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                layoutParams.x = animator.animatedValue as Int
                runCatching { windowManager.updateViewLayout(bubbleView, layoutParams) }
            }
            start()
        }
    }

    private fun onBubbleTapped() {
        if (isListening) return
        startListening()
    }

    private fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Voice input isn't available on this device", Toast.LENGTH_SHORT).show()
            return
        }

        val recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer = recognizer
        setListeningVisual(true)

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
            override fun onPartialResults(partialResults: android.os.Bundle?) {}

            override fun onError(error: Int) {
                setListeningVisual(false)
                Toast.makeText(
                    this@FloatingBubbleService,
                    "Didn't catch that — try again",
                    Toast.LENGTH_SHORT
                ).show()
                recognizer.destroy()
                speechRecognizer = null
            }

            override fun onResults(results: android.os.Bundle?) {
                setListeningVisual(false)
                val spokenText = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.trim()
                recognizer.destroy()
                speechRecognizer = null

                if (spokenText.isNullOrEmpty()) {
                    Toast.makeText(
                        this@FloatingBubbleService,
                        "Didn't catch that — try again",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    parseAndSave(spokenText)
                }
            }
        })

        val recognizeIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        recognizer.startListening(recognizeIntent)
    }

    private fun setListeningVisual(listening: Boolean) {
        isListening = listening
        bubbleBackground.setImageDrawable(if (listening) listeningDrawable else idleDrawable)
        if (listening) {
            bubbleIcon.visibility = View.GONE
            bubbleLottie.visibility = View.VISIBLE
            bubbleLottie.playAnimation()
        } else {
            bubbleLottie.cancelAnimation()
            bubbleLottie.visibility = View.GONE
            bubbleIcon.visibility = View.VISIBLE
        }
    }

    private fun parseAndSave(spokenText: String) {
        serviceScope.launch {
            val categories = categoryDao.getAll()
            val categoryNames = categories.map { it.name }

            // Gemini first, Groq as a second free-tier provider - same pattern
            // ChatViewModel already uses so one provider's rate limit doesn't block this.
            val parsed = try {
                GeminiVoiceExpenseParser.parse(spokenText, categoryNames)
            } catch (geminiError: Exception) {
                Log.w(TAG, "Gemini voice parse failed, trying Groq", geminiError)
                try {
                    GroqVoiceExpenseParser.parse(spokenText, categoryNames)
                } catch (groqError: Exception) {
                    Log.w(TAG, "Groq voice parse failed too", groqError)
                    Toast.makeText(
                        this@FloatingBubbleService,
                        "Couldn't reach the parser: ${groqError.javaClass.simpleName} — ${groqError.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }
            }
            if (parsed.amount <= 0) {
                Toast.makeText(
                    this@FloatingBubbleService,
                    "Heard “$spokenText” but no amount — try again",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            val matchedCategory = parsed.category?.let { cat ->
                categories.firstOrNull { it.name.equals(cat, ignoreCase = true) }
            }
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val request = AddExpenseRequest(
                amount = parsed.amount.toString(),
                categoryId = matchedCategory?.id.orEmpty(),
                currency = "INR",
                date = today,
                note = parsed.note.ifBlank { "Voice quick add" },
                receiptUrl = ""
            )

            when (val result = repository.addExpenseLocal(request)) {
                is UiState.Success -> Toast.makeText(
                    this@FloatingBubbleService,
                    "✓ ₹${parsed.amount} • ${matchedCategory?.name ?: "Other"}",
                    Toast.LENGTH_SHORT
                ).show()
                is UiState.Error -> Toast.makeText(
                    this@FloatingBubbleService,
                    "Couldn't save: ${result.message}",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
        snapAnimator?.cancel()
        serviceScope.cancel()
        runCatching {
            if (::bubbleView.isInitialized) windowManager.removeView(bubbleView)
        }
    }

    companion object {
        const val KEY_ENABLED = "voice_bubble_enabled"

        fun start(context: Context) {
            val intent = Intent(context, FloatingBubbleService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, FloatingBubbleService::class.java))
        }
    }
}

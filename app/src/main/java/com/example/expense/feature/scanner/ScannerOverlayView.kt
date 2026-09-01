package com.example.expense.feature.scanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Dims the camera preview outside a centered rounded-rect "scan frame",
 * matching the standard scanner/QR overlay technique (offscreen layer +
 * PorterDuff.CLEAR cutout so the frame interior stays fully visible).
 */
class ScannerOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val dimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#99000000")
    }

    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.WHITE
    }

    private val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#FF7C6FDD") // accent_purple
    }

    private var frameRect = RectF()
    private val cornerLen = 60f
    private val cornerRadius = 28f

    fun frameBounds(): RectF = frameRect

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val frameWidth = w * 0.85f
        val frameHeight = h * 0.55f
        val left = (w - frameWidth) / 2f
        val top = (h - frameHeight) / 2f
        frameRect = RectF(left, top, left + frameWidth, top + frameHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val layer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val layerCanvas = Canvas(layer)
        layerCanvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
        layerCanvas.drawRoundRect(frameRect, cornerRadius, cornerRadius, clearPaint)
        canvas.drawBitmap(layer, 0f, 0f, null)
        layer.recycle()

        canvas.drawRoundRect(frameRect, cornerRadius, cornerRadius, framePaint)
        drawCorners(canvas)
    }

    private fun drawCorners(canvas: Canvas) {
        val l = frameRect.left
        val t = frameRect.top
        val r = frameRect.right
        val b = frameRect.bottom

        // top-left
        canvas.drawLine(l, t + cornerLen, l, t, cornerPaint)
        canvas.drawLine(l, t, l + cornerLen, t, cornerPaint)
        // top-right
        canvas.drawLine(r - cornerLen, t, r, t, cornerPaint)
        canvas.drawLine(r, t, r, t + cornerLen, cornerPaint)
        // bottom-left
        canvas.drawLine(l, b - cornerLen, l, b, cornerPaint)
        canvas.drawLine(l, b, l + cornerLen, b, cornerPaint)
        // bottom-right
        canvas.drawLine(r - cornerLen, b, r, b, cornerPaint)
        canvas.drawLine(r, b - cornerLen, r, b, cornerPaint)
    }
}

package com.example.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class LiquidAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E6E6E6")
    }

    private val path = Path()

    /** 0f = big drop, 1f = circle */
    var progress: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val centerX = w / 2f

        // tweak these to taste
        val baseRadius = 120f
        val radius = baseRadius - (60f * progress)       // shrink to circle
        val stretch = 180f * (1f - progress)              // drop tail reduces

        val top = 40f
        val bottom = top + radius * 2 + stretch

        path.reset()
        path.moveTo(centerX, top)

        // left curve
        path.cubicTo(
            centerX - radius, top + stretch * 0.5f,
            centerX - radius, bottom,
            centerX, bottom
        )

        // right curve
        path.cubicTo(
            centerX + radius, bottom,
            centerX + radius, top + stretch * 0.5f,
            centerX, top
        )

        canvas.drawPath(path, paint)
    }
}
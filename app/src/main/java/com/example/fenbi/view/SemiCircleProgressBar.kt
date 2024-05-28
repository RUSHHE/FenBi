package com.example.fenbi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.google.android.material.color.MaterialColors

class SemiCircleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimaryContainer, Color.GRAY)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, Color.BLUE)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val rectF = RectF()
    var progress: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    var max: Int = 100

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Calculate the dimensions to ensure only the top half of the circle is drawn
        val diameter = w.coerceAtMost(h)
        val left = (w - diameter) / 2f
        val top = (h - diameter / 2f) / 2f
        val right = left + diameter
        val bottom = top + diameter
        rectF.set(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw background arc
        canvas.drawArc(rectF, 180f, 180f, false, backgroundPaint)
        // Draw progress arc
        val sweepAngle = (180 * progress) / max.toFloat()
        canvas.drawArc(rectF, 180f, sweepAngle, false, progressPaint)
    }
}
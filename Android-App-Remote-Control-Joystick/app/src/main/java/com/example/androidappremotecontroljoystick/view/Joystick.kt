package com.example.androidappremotecontroljoystick.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class Joystick @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // paramater for drawing
    private val bigCircle = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
    }
    private val littleCircle = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
    }
    private var smallRadius: Float = 0f
    private var smallCenter: PointF = PointF()
    private var bigRadius: Float = 0f
    private var bigCenter: PointF = PointF()
    var aileron: Float = 0f
    var elevator: Float = 0f
    lateinit var onChange: (Float, Float) -> Unit
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        smallRadius = 0.1f * min(w, h)
        smallCenter = PointF(width / 2.0f, height / 2.0f)
        bigRadius = 0.3f * min(w, h)
        bigCenter = PointF(width / 2.0f, height / 2.0f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(bigCenter.x, bigCenter.y, bigRadius, bigCircle)
        canvas.drawCircle(smallCenter.x, smallCenter.y, smallRadius, littleCircle)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> touchUp(event.x, event.y)
        }
        return true
    }

    private fun touchMove(x: Float, y: Float) {
        val distanceX = abs(x - bigCenter.x)
        val distanceY = abs(y - bigCenter.y)
        if ((bigRadius >= distanceX) && (bigRadius >= distanceY)) {
            smallCenter.x = x;
            smallCenter.y = y;

            aileron = (x - bigCenter.x) / (bigRadius)
            elevator = (y - bigCenter.y) / (bigRadius)

            try{
                onChange(aileron,elevator)
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
        invalidate()
    }

    private fun touchUp(x: Float, y: Float) {
        smallCenter.x = bigCenter.x;
        smallCenter.y = bigCenter.y;
        invalidate()
    }
}
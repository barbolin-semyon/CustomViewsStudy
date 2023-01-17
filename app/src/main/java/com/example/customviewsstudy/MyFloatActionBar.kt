package com.example.customviewsstudy

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnRepeat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.drawable.toBitmap
import java.lang.Float.min

class MyFloatActionBar(
    context: Context,
    private val attributeSet: AttributeSet?,
    private val defStyleAttr: Int,
    private val defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private lateinit var mainIconBitmap: Bitmap
    private lateinit var leftIconBitmap: Bitmap
    private lateinit var topIconBitmap: Bitmap
    private lateinit var diagonallyIconBitmap: Bitmap

    private var circleColor = 0

    private var mainCenterX = 0f
    private var mainCenterY = 0f

    private var leftCenterX = 0f
    private var topCenterY = 0f

    private var leftTopCenterX = 0f
    private var leftTopCenterY = 0f
    private var diagonaly = 0f

    private lateinit var moveToLeftValueAnimator: ValueAnimator
    private lateinit var moveToTopValueAnimator: ValueAnimator
    private lateinit var moveToDiagonallyValueAnimator: ValueAnimator
    private lateinit var increaseValueAnimator: ValueAnimator

    private val DURATION: Long = 500

    private lateinit var paint: Paint

    private var mainRadius = 0f
    private var smallRadius = 0f

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.fub_f)

    constructor(context: Context, attributeSet: AttributeSet?)
            : this(context, attributeSet, R.attr.fub_attr)

    constructor(context: Context)
            : this(context, null)

    init {
        if (attributeSet != null) initAttributes(attributeSet, defStyleAttr, defStyleRes)
        initPaint()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var width = (w - paddingStart - paddingEnd).toFloat()
        var height = (h - paddingBottom - paddingTop).toFloat()

        width /= 4
        height /= 4

        mainCenterX = width * 3f
        mainCenterY = height * 3f

        mainRadius = min(width, height) / 1.3f
        initAnimators()

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas!!.drawBitmap(mainIconBitmap, matrix, Paint())
        canvas!!.drawCircle(mainCenterX - diagonaly, mainCenterY - diagonaly, smallRadius, paint)
        canvas.drawCircle(mainCenterX, topCenterY, smallRadius, paint)
        canvas.drawCircle(leftCenterX, mainCenterY, smallRadius, paint)
        canvas.drawCircle(mainCenterX, mainCenterY, mainRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (mainCenterX - mainRadius <= event.x && event.x <= mainCenterX + mainRadius) {
                if (mainCenterY - mainRadius <= event.y && event.y <= mainCenterY + mainRadius) {
                    if (smallRadius == 0f) {
                        startMyAnimation()
                    } else {
                        reverseMyAnimations()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startMyAnimation() {
        moveToLeftValueAnimator.start()
        moveToTopValueAnimator.start()
        moveToDiagonallyValueAnimator.start()
        increaseValueAnimator.start()
    }

    private fun reverseMyAnimations() {
        moveToLeftValueAnimator.reverse()
        moveToTopValueAnimator.reverse()
        moveToDiagonallyValueAnimator.reverse()
        increaseValueAnimator.reverse()
    }

    private fun initPaint() {
        paint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color = circleColor
            style = Paint.Style.FILL
        }
    }

    @SuppressLint("Recycle")
    private fun initAnimators() {
        moveToLeftValueAnimator =
            ValueAnimator.ofFloat(mainCenterX, mainCenterX - mainRadius * 3f).apply {
                duration = DURATION
                interpolator = LinearInterpolator()
                addUpdateListener {
                    leftCenterX = animatedValue as Float
                    invalidate()
                }
            }

        moveToTopValueAnimator =
            ValueAnimator.ofFloat(mainCenterY, mainCenterY - mainRadius * 3f).apply {
                duration = DURATION
                interpolator = LinearInterpolator()
                addUpdateListener {
                    topCenterY = animatedValue as Float
                    invalidate()
                }
            }

        moveToDiagonallyValueAnimator = ValueAnimator.ofFloat(0f, mainRadius * 2.2f).apply {
            duration = DURATION
            interpolator = LinearInterpolator()
            addUpdateListener {
                diagonaly = animatedValue as Float
                invalidate()
            }
        }

        increaseValueAnimator = ValueAnimator.ofFloat(0f, mainRadius / 1.5f).apply {
            duration = DURATION
            interpolator = LinearInterpolator()
            addUpdateListener {
                smallRadius = animatedValue as Float
                invalidate()
            }
        }

    }

    @SuppressLint("CustomViewStyleable", "Recycle")
    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val arrayAttr = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.loader_styleable,
            defStyleAttr, defStyleRes
        )

        circleColor = arrayAttr.getColor(R.styleable.fub_attr_color, Color.BLUE)
        //mainIconBitmap = arrayAttr.getDrawable(R.styleable.fub_attr_main_icon)!!.toBitmap(100, 100)

    }

}
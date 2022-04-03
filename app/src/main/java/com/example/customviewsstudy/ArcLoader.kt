package com.example.customviewsstudy

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import kotlin.math.min

class ArcLoader(
    context: Context,
    val attributeSet: AttributeSet?,
    val defStyleAttr: Int,
    val defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private lateinit var paint: Paint
    private lateinit var paintTwo: Paint

    private var width: Float = 0f

    private var centerX = 0f
    private var centerY = 0f

    private var radiusOne = 0f
    private var radiusTwo = 0f
    private var radiusThree = 0f

    private var sweepAngle = 360f
    private var startAngle = 0f

    private var colorArc = 0
    private var colorArcTwo = 0

    private lateinit var valueAnimatorSweep: ValueAnimator
    private lateinit var valueAnimatorStart: ValueAnimator

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.purpleLoaderStyle)

    constructor(context: Context, attributeSet: AttributeSet?)
            : this(context, attributeSet, R.attr.loader_styleable)

    constructor(context: Context)
            : this(context, null)

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        }

        initAnimator()
        valueAnimatorSweep.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val widthCanvas = w - paddingStart - paddingEnd
        val heightCanvas = h - paddingTop - paddingBottom

        centerX = widthCanvas / 2f
        centerY = heightCanvas / 2f

        radiusOne = min(widthCanvas, heightCanvas) / 7f
        radiusTwo = radiusOne
        radiusTwo = radiusOne * 2
        radiusThree = radiusOne * 3
        width = radiusOne / 5

        initPaint()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!.drawArc(
            centerX - radiusOne,
            centerY - radiusOne,
            centerX + radiusOne,
            centerY + radiusOne,
            startAngle, sweepAngle,
            false, paint)

        canvas!!.drawArc(
            centerX - radiusTwo,
            centerY - radiusTwo,
            centerX + radiusTwo,
            centerY + radiusTwo,
            -startAngle, -sweepAngle,
            false, paintTwo)

        canvas!!.drawArc(
            centerX - radiusThree,
            centerY - radiusThree,
            centerX + radiusThree,
            centerY + radiusThree,
            startAngle, sweepAngle,
            false, paint)

        super.onDraw(canvas)
    }

    private fun initPaint() {
        paint = Paint(ANTI_ALIAS_FLAG).apply {
            color = colorArc
            style = Paint.Style.STROKE
            strokeWidth = width
        }

        paintTwo = Paint().apply {
            set(paint)
            color = colorArcTwo
        }
    }

    private fun initAnimator() {
        valueAnimatorSweep = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.RESTART
            doOnRepeat { valueAnimatorStart.start() }

            addUpdateListener {
                sweepAngle = animatedValue as Float
                invalidate()
            }
        }

        valueAnimatorStart = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1300
            interpolator = LinearInterpolator()

            addUpdateListener {
                startAngle = animatedValue as Float
                invalidate()
            }
        }

    }

    @SuppressLint("CustomViewStyleable", "Recycle")
    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val attrArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.loader_styleable,
            defStyleAttr, defStyleRes
        )

        colorArc = attrArray.getColor(R.styleable.loader_styleable_loader_color, Color.BLUE)
        colorArcTwo = attrArray.getColor(R.styleable.loader_styleable_two_loader_color, Color.BLUE)
    }

}
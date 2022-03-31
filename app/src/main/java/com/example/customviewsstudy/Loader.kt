package com.example.customviewsstudy

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sqrt

class Loader(
    context: Context,
    private val attributeSet: AttributeSet?,
    private val defStyleAttr: Int,
    private val defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var colorLoader: Int = Color.BLACK
    private var colorLoaderTwo: Int = Color.BLACK

    private var centerX = 0
    private var centerY = 0
    private var space = 0f
    private var sizeRect = 0

    private var angle = 0f

    private lateinit var oneRect: Path
    private lateinit var twoRect: Path

    private val oneMatrix = Matrix()
    private val twoMatrix = Matrix()

    private lateinit var paint: Paint
    private lateinit var twoPaint: Paint

    private lateinit var angleAnimator: ValueAnimator
    private lateinit var moveAnimator: ValueAnimator

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.purpleLoaderStyle)

    constructor(context: Context, attributeSet: AttributeSet?)
            : this(context, attributeSet, R.attr.loader_styleable)

    constructor(context: Context)
            : this(context, null)

    init {
        if (attributeSet != null) initAttributes(attributeSet, defStyleAttr, defStyleRes)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        oneRect.transform(oneMatrix)
        twoRect.transform(twoMatrix)
        canvas!!.drawPath(oneRect, paint)
        canvas.drawPath(twoRect, twoPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val width = w - paddingLeft - paddingRight
        val height = h - paddingBottom - paddingTop
        val fullSize = if (width < height) width else height

        sizeRect = sqrt(2 * Math.pow((fullSize / 5).toDouble(), 2.0)).toInt()
        space = sizeRect / 1.5f
        centerX = width / 2
        centerY = height / 2

        initShapes()
        initAnimator()
        angleAnimator.start()
        //moveAni  mator.start()
    }

    private fun initShapes() {
        paint = Paint().apply {
            color = colorLoader
            strokeWidth = space / 5f
            pathEffect = DashPathEffect(floatArrayOf(20f, 5f), 30f)
            style = Paint.Style.STROKE
        }

        twoPaint = Paint().apply {
            color = colorLoaderTwo
            strokeWidth = space / 5f
            pathEffect = DashPathEffect(floatArrayOf(20f, 5f), 30f)
            style = Paint.Style.STROKE
        }

        oneRect = Path().apply {
            addCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                sizeRect.toFloat(),
                Path.Direction.CW
            )
        }

        twoRect = Path().apply {
            addCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                space.toFloat(),
                Path.Direction.CW
            )
        }
    }

    private fun initAnimator() {
        angleAnimator = ValueAnimator.ofInt (0, 7).apply {
            duration = 2000
            repeatCount = 1000
            repeatMode = ValueAnimator.REVERSE

            interpolator = LinearInterpolator()
            addUpdateListener {

                angle = animatedValue as Int + 0f
                oneMatrix.setRotate(angle, centerX.toFloat(), centerY.toFloat());
                twoMatrix.setRotate(-angle, centerX.toFloat(), centerY.toFloat());
                invalidate()
            }
        }
    }

    private fun initAttributes(attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val arrayAttributes = context.obtainStyledAttributes(
            attributeSet, R.styleable.loader_styleable, defStyleAttr, defStyleRes
        )
        colorLoader =
            arrayAttributes.getColor(R.styleable.loader_styleable_loader_color, Color.BLACK)

        colorLoaderTwo =
            arrayAttributes.getColor(R.styleable.loader_styleable_two_loader_color, Color.BLACK)
    }

}
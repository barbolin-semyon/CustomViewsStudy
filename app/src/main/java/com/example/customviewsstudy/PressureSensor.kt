package com.example.customviewsstudy

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator

class PressureSensor
    (
    context: Context,
    private val attributeSet: AttributeSet?,
    private val defStyleAttr: Int,
    private val defStyleRes: Int
    ) : View(context, attributeSet, defStyleAttr, defStyleRes)
{
    private var widthProgress: Float = 0f
    private var heigthProgress: Float = 0f
    private var startX: Float = 0f
    private var startY: Float = 0f

    private var colorBackground = DEFAULT_BACKGROUND_COLOR;
    private var colorBottom = DEFAULT_BACKGROUND_COLOR_BOTTOM;
    private var colorUp = DEFAULT_BACKGROUND_COLOR_UP;
    private var colorBtn = DEFAULT_BACKGROUND_COLOR_BTN;
    private var colorTextBtn = DEFAULT_BACKGROUND_COLOR_TEXT_BTN;

    private lateinit var paintBackgroundPaint: Paint;
    private lateinit var paintProgressPaint: Paint;
    private lateinit var paintButton: Paint;
    private lateinit var paintTextButton: Paint;

    private lateinit var backgroundRect: RectF
    private lateinit var path: Path

    lateinit var valueAnimatorToUp: ValueAnimator

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.purplePressureSensor)

    constructor(context: Context, attributeSet: AttributeSet?)
    : this(context, attributeSet, R.attr.pressure_sensor)

    constructor(context: Context): this(context, null)

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthProgress = (w - paddingLeft - paddingRight).toFloat()
        heigthProgress = (h - paddingTop - paddingBottom).toFloat()
        startX = 0f
        startY = 0f

        initPaint()
        initShapes()
        initAnimators()
    }

    private fun initAnimators() {
        valueAnimatorToUp = ValueAnimator.ofFloat(heigthProgress + startY, startY).apply {
            duration = 300
            repeatCount = 100
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            addUpdateListener {
                backgroundRect.top = it.animatedValue as Float
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paintBackgroundPaint)
        canvas?.clipPath(path)
        canvas?.drawRoundRect(backgroundRect, 16f, 16f, paintProgressPaint)
    }

    @SuppressLint("Recycle")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_DOWN -> valueAnimatorToUp.start()
            MotionEvent.ACTION_UP -> valueAnimatorToUp.pause()
        }

        return true
    }

    private fun initShapes() {
        backgroundRect = RectF(
            startX,
            startY,
            startX + widthProgress,
            startY + heigthProgress
        )

        path = Path().apply {
            moveTo(startX, startY)
            lineTo(startX + widthProgress, startY)
            lineTo(startX + widthProgress, startY + heigthProgress)
            lineTo(startX, startY + heigthProgress)
            lineTo(startX, startY)
            lineTo(startX + widthProgress, startY)
        }
    }

    private fun initPaint() {
        paintBackgroundPaint = Paint().apply {
            isAntiAlias = true
            pathEffect = CornerPathEffect(30f)
            color = colorBackground
        }

        paintProgressPaint = Paint().apply {
            isAntiAlias = true
            val gradient = LinearGradient(
                startX, startY,
                startX + widthProgress,
                startY + heigthProgress,
                colorUp, colorBottom,
                Shader.TileMode.MIRROR
            )
            shader = gradient

        }
    }

    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable .pressure_sensor,
            defStyleAttr, defStyleRes
        )

        colorBackground = typedArray.getColor(
            R.styleable.pressure_sensor_background_color,
            DEFAULT_BACKGROUND_COLOR
        )

        colorUp = typedArray.getColor(
            R.styleable.pressure_sensor_background_color_up,
            DEFAULT_BACKGROUND_COLOR_UP
        )

        colorBottom = typedArray.getColor(
            R.styleable.pressure_sensor_background_color_bottom,
            DEFAULT_BACKGROUND_COLOR_BOTTOM
        )

        colorBtn = typedArray.getColor(
            R.styleable.pressure_sensor_background_color_btn,
            DEFAULT_BACKGROUND_COLOR_BTN
        )

        colorTextBtn = typedArray.getColor(
            R.styleable.pressure_sensor_background_color_text_btn,
            colorTextBtn
        )
    }

    companion object {
        const val DEFAULT_BACKGROUND_COLOR = Color.GRAY
        const val DEFAULT_BACKGROUND_COLOR_BOTTOM = Color.CYAN
        const val DEFAULT_BACKGROUND_COLOR_UP = Color.BLUE
        const val DEFAULT_BACKGROUND_COLOR_BTN = Color.BLACK
        const val DEFAULT_BACKGROUND_COLOR_TEXT_BTN = Color.WHITE
    }
}
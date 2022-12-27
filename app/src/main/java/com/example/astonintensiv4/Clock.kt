package com.example.astonintensiv4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var heightOfClock = 0
    private var widthOfClock = 0
    private var centerX = 0
    private var centerY = 0
    private var padding = 0
    private var handTruncation = 0
    private var hourHandLength = 0
    private var minuteHandLength = 0
    private var secondHandLength = 0
    private var radius = 0
    private var paint = Paint()
    private var isInit = false
    private val numbers = 1..12
    private val rect = Rect()
    private var colorOfHandHour = 0
    private var colorOfHandMinute = 0
    private var colorOfHandSeconds = 0
    private var circleColor = 0
    private var centerColor = 0

    init {
        context.withStyledAttributes(attrs, R.styleable.Clock, defStyleAttr, 0) {
            colorOfHandHour = getColor(R.styleable.Clock_hours_hand_color, DEFAULT_COLOR)
            colorOfHandMinute = getColor(R.styleable.Clock_minutes_hand_color, DEFAULT_COLOR)
            colorOfHandSeconds = getColor(R.styleable.Clock_seconds_hand_color, DEFAULT_COLOR)
            padding = getDimension(R.styleable.Clock_padding, DEFAULT_PADDING).toInt()
            centerColor = getColor(R.styleable.Clock_center_color, DEFAULT_COLOR)
            circleColor = getColor(R.styleable.Clock_circle_color, DEFAULT_COLOR)
            hourHandLength = getInt(R.styleable.Clock_length_hours_hand, 100)
            minuteHandLength = getInt(R.styleable.Clock_length_minutes_hand, 100)
            secondHandLength = getInt(R.styleable.Clock_length_seconds_hand, 100)

        }
    }

    private fun initializationClock() {
        heightOfClock = height
        widthOfClock = width
        centerX = width / 2
        centerY = height / 2
        val min = min(heightOfClock, widthOfClock)
        radius = min / 2 - padding
        handTruncation = min / 20
        isInit = true
    }


    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initializationClock()
        }
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(1000)
        invalidate()
    }

    private fun drawHand(canvas: Canvas, time: Double, isHour: Boolean, isMinute: Boolean) {
        val angle = Math.PI * time / 30 - Math.PI / 2
        val lengthOfHand =
            if (isHour) {
                paint.apply {
                    reset()
                    color = colorOfHandHour
                    isAntiAlias = true
                    strokeWidth = STROKE_WIDTH_HAND_HOUR
                }
                hourHandLength
            } else if (isMinute) {
                paint.apply {
                    reset()
                    color = colorOfHandMinute
                    isAntiAlias = true
                    strokeWidth = STROKE_WIDTH_HAND_MINUTE
                }
                minuteHandLength
            } else {
                paint.apply {
                    reset()
                    color = colorOfHandSeconds
                    isAntiAlias = true
                    strokeWidth = STROKE_WIDTH_HAND_SECOND
                }
                secondHandLength
            }
        canvas.drawLine(
            (centerX).toFloat(),
            (centerY).toFloat(),
            (centerX + cos(angle) * lengthOfHand).toFloat(),
            (centerY + sin(angle) * lengthOfHand).toFloat(),
            paint
        )
    }


    private fun drawCircle(canvas: Canvas) {
        paint.apply {
            reset()
            color = circleColor
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawCircle(
            (centerX).toFloat(), (centerY).toFloat(),
            (radius + padding - 10).toFloat(), paint
        )
    }

    private fun drawCenter(canvas: Canvas) {
        paint.apply {
            reset()
            color = centerColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(
            (centerX).toFloat(),
            (centerY).toFloat(),
            12F, paint
        )
    }


    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR).toFloat()
        drawHand(
            canvas, ((hour + calendar.get(Calendar.MINUTE) / 60) * 5f).toDouble(),
            isHour = true,
            isMinute = false
        )
        drawHand(canvas, calendar.get(Calendar.MINUTE).toDouble(), isHour = false, isMinute = true)
        drawHand(canvas, calendar.get(Calendar.SECOND).toDouble(), isHour = false, isMinute = false)
    }


    private fun drawNumeral(canvas: Canvas) {
        numbers.forEach {
            paint.apply {
                reset()
                isAntiAlias = true
                strokeWidth = 5f
                color = Color.BLACK
            }
            val corner = Math.PI / 6 * (it - 3)
            canvas.drawLine(
                (centerX + cos(corner) * radius - rect.width() / 2).toFloat(),
                (centerY + sin(corner) * radius + rect.height() / 2).toFloat(),
                (centerX + cos(corner) * (radius + DEFAULT_LONG_OF_NUMERAL)).toFloat(),
                (centerY + sin(corner) * (radius + DEFAULT_LONG_OF_NUMERAL)).toFloat(),
                paint

            )
        }
    }

    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
        private const val STROKE_WIDTH_HAND_HOUR = 10f
        private const val STROKE_WIDTH_HAND_MINUTE = 7f
        private const val STROKE_WIDTH_HAND_SECOND = 3f
        private const val DEFAULT_LONG_OF_NUMERAL = 40
        private const val DEFAULT_PADDING = 50F
    }


}

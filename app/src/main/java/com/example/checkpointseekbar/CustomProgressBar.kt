package com.example.checkpointseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.widget.ProgressBar
import kotlin.math.abs


class CustomProgressBar : ProgressBar {
    private var mTickMark: Drawable? = null
    private var mlockedMark: Drawable? = null
    private var mTopLabelDrawable: Drawable? = null
    private var mBottomLabelDrawable: Drawable? = null
    var mCheckPoints: ArrayList<CheckPoint>? = null
    var mPaddingTextTick = 10
    var SELECTED_TEXT_COLOR = Color.GREEN
    var TEXT_COLOR = Color.GRAY
    var TEXT_SIZE = 30
    var topLabel = ""
    var bottomLabel = ""
    var secondBottomLabel = ""
    private var secondBottomLabelDrawable: Drawable? = null

    companion object {
        val textPaint = Paint()

        val textPaintSelected = Paint()
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        initPaints()

        getResourcesFromTypeAttributes(attrs, defStyle)
    }

    private fun initPaints() {
        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true
        textPaint.color = TEXT_COLOR
        textPaint.textAlign = Paint.Align.CENTER

        textPaintSelected.style = Paint.Style.FILL
        textPaintSelected.isAntiAlias = true
        textPaintSelected.color = SELECTED_TEXT_COLOR
        textPaintSelected.textAlign = Paint.Align.CENTER
    }

    private fun getResourcesFromTypeAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyle, 0)

        mTickMark = a.getDrawable(R.styleable.CustomProgressBar_tick_drawable)
        mlockedMark = a.getDrawable(R.styleable.CustomProgressBar_lock_drawable)
        mBottomLabelDrawable = a.getDrawable(R.styleable.CustomProgressBar_bottom_label_drawable)
        mTopLabelDrawable = a.getDrawable(R.styleable.CustomProgressBar_top_label_drawable)
        mlockedMark = a.getDrawable(R.styleable.CustomProgressBar_lock_drawable)
        mPaddingTextTick =
            a.getDimension(R.styleable.CustomProgressBar_label_padding, mPaddingTextTick.toFloat()).toInt()
        SELECTED_TEXT_COLOR = a.getColor(R.styleable.CustomProgressBar_selected_text_color, SELECTED_TEXT_COLOR)
        TEXT_COLOR = a.getColor(R.styleable.CustomProgressBar_text_color, TEXT_COLOR)
        TEXT_SIZE = a.getDimensionPixelSize(R.styleable.CustomProgressBar_text_size, TEXT_SIZE)
        val fontName = a.getResourceId(R.styleable.CustomProgressBar_font_family, -1)
        if (fontName != -1) {
            val tf = ResourcesCompat.getFont(context, fontName)
            textPaint.typeface = tf
            textPaintSelected.typeface = tf
        }
        topLabel = a.getString(R.styleable.CustomProgressBar_top_label) ?: ""
        bottomLabel = a.getString(R.styleable.CustomProgressBar_bottom_label) ?: ""
        secondBottomLabel = a.getString(R.styleable.CustomProgressBar_second_bottom_label) ?: ""
        secondBottomLabelDrawable = a.getDrawable(R.styleable.CustomProgressBar_second_bottom_label_drawable)
        secondBottomLabelDrawable = ContextCompat.getDrawable(context, R.drawable.ic_touch_points)
        a.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLabels(canvas)
        drawTicks(canvas)
    }

    private fun drawLabels(canvas: Canvas?) {
        canvas?.let { nonNullCanvas ->
            var saveCount = nonNullCanvas.save()
            nonNullCanvas.translate(0f, (height / 2).toFloat())
            var h = mTickMark?.intrinsicHeight
            h = if (h!! >= 0) h else 1
            val bounds = Rect()
            if (topLabel.isNotEmpty()) {
                textPaint.getTextBounds(topLabel, 0, topLabel.length, bounds)
                if (mTopLabelDrawable != null) {
                    val drawableWidth = mTopLabelDrawable?.intrinsicWidth

                    val drawableHeight = mTopLabelDrawable?.intrinsicHeight
                    val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                    val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                    mTopLabelDrawable?.setBounds(-halfW, -halfH, halfW, halfH)

                    nonNullCanvas.translate(
                        halfW.toFloat(),
                        (-1 * (mPaddingTextTick + h / 2 + halfH)).toFloat()
                    )
                    mTopLabelDrawable?.draw(nonNullCanvas)
                    nonNullCanvas.translate(
                        bounds.exactCenterX() + halfW,
                        ((mPaddingTextTick/2 )).toFloat()
                    )
                } else {
                    nonNullCanvas.translate(
                        bounds.exactCenterX(),
                        (-1 * (mPaddingTextTick + h / 2 + TEXT_SIZE / 2)).toFloat()
                    )
                }
                nonNullCanvas.drawText(topLabel, 0f, 0f, textPaint)
            }
            nonNullCanvas.restoreToCount(saveCount)
            saveCount = nonNullCanvas.save()
            nonNullCanvas.translate(0f, (height / 2).toFloat())
            if (bottomLabel.isNotEmpty()) {
                textPaint.getTextBounds(bottomLabel, 0, bottomLabel.length, bounds)
                if (mBottomLabelDrawable != null) {
                    val drawableWidth = mBottomLabelDrawable?.intrinsicWidth

                    val drawableHeight = mBottomLabelDrawable?.intrinsicHeight
                    val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                    val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                    mBottomLabelDrawable?.setBounds(-2, -2, 2, 2)

                    nonNullCanvas.translate(
                        halfW.toFloat(),
                        (mPaddingTextTick + h / 2 + TEXT_SIZE - halfH).toFloat()
                    )
                    mBottomLabelDrawable?.draw(nonNullCanvas)
                    nonNullCanvas.translate(
                        bounds.exactCenterX() + halfW,
                        abs(halfH.toFloat())
                    )
                } else {
                    nonNullCanvas.translate(bounds.exactCenterX(), (mPaddingTextTick + h / 2 + TEXT_SIZE).toFloat())
                }

                nonNullCanvas.drawText(bottomLabel, 0f, 0f, textPaint)
            }
            nonNullCanvas.restoreToCount(saveCount)
        }
    }

    public fun setCheckPoints(checkPoints: ArrayList<CheckPoint>) {
        mCheckPoints = checkPoints
        mCheckPoints?.sortedBy { it.progress }
        invalidate()
    }


    private fun drawTicks(canvas: Canvas?) {
        canvas?.let { nonNullCanvas ->
            if (!mCheckPoints.isNullOrEmpty()) {
                mCheckPoints?.forEachIndexed { index, it ->

                    val saveCount = nonNullCanvas.save()
                    nonNullCanvas.translate(0f, (height / 2).toFloat())
                    val h: Int?
                    if (it.progress <= progress) {
                        val w = mTickMark?.intrinsicWidth

                        h = mTickMark?.intrinsicHeight
                        val halfW = if (w!! >= 0) w / 2 else 1
                        val halfH = if (h!! >= 0) h / 2 else 1
                        mTickMark?.setBounds(-halfW, -halfH, halfW, halfH)

                        var spacing = ((width.toDouble() / max.toDouble()) * it.progress)
                        if (spacing < w / 2)
                            spacing = (w / 2).toDouble()
                        if (it.progress > max / 2) {
                            spacing -= halfW
                        }
                        nonNullCanvas.translate(spacing.toFloat(), 0f)
                        mTickMark?.draw(nonNullCanvas)
                    } else {
                        val w = mlockedMark?.intrinsicWidth
                        h = mlockedMark?.intrinsicHeight
                        val halfW = if (w!! >= 0) w / 2 else 1
                        val halfH = if (h!! >= 0) h / 2 else 1
                        mlockedMark?.setBounds(-halfW, -halfH, halfW, halfH)

                        var spacing = ((width.toDouble() / max.toDouble()) * it.progress)

                        if (spacing < w / 2)
                            spacing = (w / 2).toDouble()
                        if (it.progress > max / 2) {
                            spacing -= halfW
                        }
                        nonNullCanvas.translate(spacing.toFloat(), 0f)
                        mlockedMark?.draw(nonNullCanvas)
                    }
                    nonNullCanvas.translate(0f, -1 * (mPaddingTextTick + h / 2 + TEXT_SIZE / 2).toFloat())
                    it.topText?.let { topText ->
                        mCheckPoints?.findClosest(progress)?.let { pair ->
                            if (index == pair.first) {
                                nonNullCanvas.drawText(topText, 0f, 0f, textPaintSelected)
                            } else {
                                nonNullCanvas.drawText(topText, 0f, 0f, textPaint)
                            }

                        } ?: nonNullCanvas.drawText(topText, 0f, 0f, textPaint)
                    }
                    nonNullCanvas.translate(0f, (1.5 * TEXT_SIZE + 2 * mPaddingTextTick + h).toFloat())
                    it.bottomText?.let { bottomText ->
                        val bounds = Rect()
                        textPaint.getTextBounds(bottomText, 0, bottomText.length, bounds)
                        nonNullCanvas.drawText(bottomText, 0f, 0f, textPaint)
                        if (secondBottomLabelDrawable != null) {
                            val drawableWidth = secondBottomLabelDrawable?.intrinsicWidth

                            val drawableHeight = secondBottomLabelDrawable?.intrinsicHeight
                            val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                            val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                            secondBottomLabelDrawable?.setBounds(-halfW, -halfH, halfW, halfH)

                            nonNullCanvas.translate(
                                halfW.toFloat() / 2 + bounds.right,
                                -halfH.toFloat() + TEXT_SIZE / 4
                            )
                            secondBottomLabelDrawable?.draw(nonNullCanvas)
                            nonNullCanvas.translate(
                                -(halfW.toFloat() / 2 + bounds.right),
                                halfH.toFloat() - TEXT_SIZE / 4
                            )
                        }
                    }

                    it.secondBottomText?.let { secondBottomText ->
                        val bounds = Rect()

                        textPaint.getTextBounds(secondBottomText, 0, secondBottomText.length, bounds)
                        nonNullCanvas.drawText(secondBottomText, 0f, 60f, textPaint)
                    }
                    nonNullCanvas.restoreToCount(saveCount)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        mTickMark?.let {
            var h = it.intrinsicHeight
            h += height + mPaddingTextTick + TEXT_SIZE
            if (height < h) {
                height = h
            }
            textPaint.textSize = TEXT_SIZE.toFloat()
            textPaintSelected.textSize = TEXT_SIZE.toFloat()
            val w = it.intrinsicWidth
            if (paddingLeft < w / 2) {
                setPadding(w / 2, paddingTop, paddingRight, paddingBottom)
            }
            if (paddingRight < w / 2) {
                setPadding(paddingLeft, paddingTop, w / 2, paddingBottom)
            }

        }
        setMeasuredDimension(width, height)
    }

    private fun java.util.ArrayList<CheckPoint>.findClosest(mProgress: Int): Pair<Int, Int>? {
        for (i in 1 until size) {
            if (mProgress >= get(i - 1).progress && mProgress <= get(i).progress) {

                return if (mProgress == get(i).progress)
                    Pair(i, get(i).progress)
                else
                    Pair(i - 1, get(i - 1).progress)

            }
        }
        return null
    }
}

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


class CheckpointProgressBar : ProgressBar {
    private var mCompletedCheckMark: Drawable? = null
    private var mIncompleteCheckMark: Drawable? = null
    private var mTopLabelLeftDrawable: Drawable? = null
    private var mBottomLabelLeftDrawable: Drawable? = null
    var mCheckPoints: ArrayList<CheckPoint>? = null
    var mCompletedCheckPointTextPadding = 10
    var SELECTED_TEXT_COLOR = Color.GREEN
    var TEXT_COLOR = Color.GRAY
    var TEXT_SIZE = 30
    var topLabel = ""
    var bottomLabel = ""
    private var checkPointSecondBottomLabelDrawable: Drawable? = null

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
        val a = context.obtainStyledAttributes(attrs, R.styleable.CheckpointProgressBar, defStyle, 0)

        mCompletedCheckMark = a.getDrawable(R.styleable.CheckpointProgressBar_ic_completed_drawable)
        mIncompleteCheckMark = a.getDrawable(R.styleable.CheckpointProgressBar_ic_incomplete_drawable)
        mBottomLabelLeftDrawable = a.getDrawable(R.styleable.CheckpointProgressBar_bottom_label_drawable)
        mTopLabelLeftDrawable = a.getDrawable(R.styleable.CheckpointProgressBar_top_label_drawable)
        mCompletedCheckPointTextPadding =
            a.getDimension(R.styleable.CheckpointProgressBar_label_padding, mCompletedCheckPointTextPadding.toFloat()).toInt()
        SELECTED_TEXT_COLOR = a.getColor(R.styleable.CheckpointProgressBar_selected_text_color, SELECTED_TEXT_COLOR)
        TEXT_COLOR = a.getColor(R.styleable.CheckpointProgressBar_text_color, TEXT_COLOR)
        TEXT_SIZE = a.getDimensionPixelSize(R.styleable.CheckpointProgressBar_text_size, TEXT_SIZE)
        val fontName = a.getResourceId(R.styleable.CheckpointProgressBar_font_family, -1)
        if (fontName != -1) {
            val tf = ResourcesCompat.getFont(context, fontName)
            textPaint.typeface = tf
            textPaintSelected.typeface = tf
        }
        topLabel = a.getString(R.styleable.CheckpointProgressBar_top_label) ?: ""
        bottomLabel = a.getString(R.styleable.CheckpointProgressBar_bottom_label) ?: ""
        checkPointSecondBottomLabelDrawable = a.getDrawable(R.styleable.CheckpointProgressBar_second_bottom_label_drawable)
        checkPointSecondBottomLabelDrawable = ContextCompat.getDrawable(context, R.drawable.ic_touch_points)
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
            var h = mCompletedCheckMark?.intrinsicHeight
            h = if (h!! >= 0) h else 1
            val bounds = Rect()
            if (topLabel.isNotEmpty()) {
                textPaint.getTextBounds(topLabel, 0, topLabel.length, bounds)
                if (mTopLabelLeftDrawable != null) {
                    val drawableWidth = mTopLabelLeftDrawable?.intrinsicWidth

                    val drawableHeight = mTopLabelLeftDrawable?.intrinsicHeight
                    val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                    val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                    mTopLabelLeftDrawable?.setBounds(-halfW, -halfH, halfW, halfH)

                    nonNullCanvas.translate(
                        halfW.toFloat(),
                        (-1 * (mCompletedCheckPointTextPadding + h / 2 + halfH)).toFloat()
                    )
                    mTopLabelLeftDrawable?.draw(nonNullCanvas)
                    nonNullCanvas.translate(
                        bounds.exactCenterX() + halfW,
                        ((mCompletedCheckPointTextPadding/2 )).toFloat()
                    )
                } else {
                    nonNullCanvas.translate(
                        bounds.exactCenterX(),
                        (-1 * (mCompletedCheckPointTextPadding + h / 2 + TEXT_SIZE / 2)).toFloat()
                    )
                }
                nonNullCanvas.drawText(topLabel, 0f, 0f, textPaint)
            }
            nonNullCanvas.restoreToCount(saveCount)
            saveCount = nonNullCanvas.save()
            nonNullCanvas.translate(0f, (height / 2).toFloat())
            if (bottomLabel.isNotEmpty()) {
                textPaint.getTextBounds(bottomLabel, 0, bottomLabel.length, bounds)
                if (mBottomLabelLeftDrawable != null) {
                    val drawableWidth = mBottomLabelLeftDrawable?.intrinsicWidth

                    val drawableHeight = mBottomLabelLeftDrawable?.intrinsicHeight
                    val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                    val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                    mBottomLabelLeftDrawable?.setBounds(-2, -2, 2, 2)

                    nonNullCanvas.translate(
                        halfW.toFloat(),
                        (mCompletedCheckPointTextPadding + h / 2 + TEXT_SIZE - halfH).toFloat()
                    )
                    mBottomLabelLeftDrawable?.draw(nonNullCanvas)
                    nonNullCanvas.translate(
                        bounds.exactCenterX() + halfW,
                        abs(halfH.toFloat())
                    )
                } else {
                    nonNullCanvas.translate(bounds.exactCenterX(), (mCompletedCheckPointTextPadding + h / 2 + TEXT_SIZE).toFloat())
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
                        val w = mCompletedCheckMark?.intrinsicWidth

                        h = mCompletedCheckMark?.intrinsicHeight
                        val halfW = if (w!! >= 0) w / 2 else 1
                        val halfH = if (h!! >= 0) h / 2 else 1
                        mCompletedCheckMark?.setBounds(-halfW, -halfH, halfW, halfH)

                        var spacing = ((width.toDouble() / max.toDouble()) * it.progress)
                        if (spacing < w / 2)
                            spacing = (w / 2).toDouble()
                        if (it.progress > max / 2) {
                            spacing -= halfW
                        }
                        nonNullCanvas.translate(spacing.toFloat(), 0f)
                        mCompletedCheckMark?.draw(nonNullCanvas)
                    } else {
                        val w = mIncompleteCheckMark?.intrinsicWidth
                        h = mIncompleteCheckMark?.intrinsicHeight
                        val halfW = if (w!! >= 0) w / 2 else 1
                        val halfH = if (h!! >= 0) h / 2 else 1
                        mIncompleteCheckMark?.setBounds(-halfW, -halfH, halfW, halfH)

                        var spacing = ((width.toDouble() / max.toDouble()) * it.progress)

                        if (spacing < w / 2)
                            spacing = (w / 2).toDouble()
                        if (it.progress > max / 2) {
                            spacing -= halfW
                        }
                        nonNullCanvas.translate(spacing.toFloat(), 0f)
                        mIncompleteCheckMark?.draw(nonNullCanvas)
                    }
                    nonNullCanvas.translate(0f, -1 * (mCompletedCheckPointTextPadding + h / 2 + TEXT_SIZE / 2).toFloat())
                    it.topText?.let { topText ->
                        mCheckPoints?.findClosest(progress)?.let { pair ->
                            if (index == pair.first) {
                                nonNullCanvas.drawText(topText, 0f, 0f, textPaintSelected)
                            } else {
                                nonNullCanvas.drawText(topText, 0f, 0f, textPaint)
                            }

                        } ?: nonNullCanvas.drawText(topText, 0f, 0f, textPaint)
                    }
                    nonNullCanvas.translate(0f, (1.5 * TEXT_SIZE + 2 * mCompletedCheckPointTextPadding + h).toFloat())
                    it.bottomText?.let { bottomText ->
                        val bounds = Rect()
                        textPaint.getTextBounds(bottomText, 0, bottomText.length, bounds)
                        nonNullCanvas.drawText(bottomText, 0f, 0f, textPaint)
                        if (checkPointSecondBottomLabelDrawable != null) {
                            val drawableWidth = checkPointSecondBottomLabelDrawable?.intrinsicWidth

                            val drawableHeight = checkPointSecondBottomLabelDrawable?.intrinsicHeight
                            val halfW = if (drawableWidth!! >= 0) drawableWidth / 2 else 1
                            val halfH = if (drawableHeight!! >= 0) drawableHeight / 2 else 1
                            checkPointSecondBottomLabelDrawable?.setBounds(-halfW, -halfH, halfW, halfH)

                            nonNullCanvas.translate(
                                halfW.toFloat() / 2 + bounds.right,
                                -halfH.toFloat() + TEXT_SIZE / 4
                            )
                            checkPointSecondBottomLabelDrawable?.draw(nonNullCanvas)
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
        mCompletedCheckMark?.let {
            var h = it.intrinsicHeight
            h += height + mCompletedCheckPointTextPadding + TEXT_SIZE
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

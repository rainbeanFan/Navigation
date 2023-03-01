package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import cn.lancet.navigation.R
import cn.lancet.navigation.dp


private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHT_LIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp

class SportView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 60.dp
        typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
        textAlign = Paint.Align.CENTER //Horizontal Center
//        isFakeBoldText = true
    }

    private val mBound = Rect()

    private val mFontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPaint.apply {
            style = Paint.Style.STROKE
            color = CIRCLE_COLOR
            strokeWidth = RING_WIDTH
        }
        canvas.drawCircle(width / 2F, height / 2F, RADIUS, mPaint)

        mPaint.apply {
            color = HIGHT_LIGHT_COLOR
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(
            width / 2F - RADIUS, height / 2F - RADIUS,
            width / 2F + RADIUS, height / 2F + RADIUS,
            -90F, 225F, false, mPaint
        )

        mPaint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#000000")
        }

        mPaint.getFontMetrics(mFontMetrics)

//        mPaint.getTextBounds("Jacky",0,"Jacky".length,mBound)
        canvas.drawText(
            "Joe phony", width / 2F,
            height / 2F - (mFontMetrics.ascent + mFontMetrics.descent) / 2F, mPaint
        )

        mPaint.textAlign = Paint.Align.LEFT
        mPaint.getFontMetrics(mFontMetrics)
//        canvas.drawText("abcd", -mBound.left.toFloat(), -mFontMetrics.top,mPaint)

        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = 10.dp
        mPaint.getFontMetrics(mFontMetrics)
//        canvas.drawText("abcd", -mBound.left.toFloat(), -mFontMetrics.top,mPaint)

    }

}
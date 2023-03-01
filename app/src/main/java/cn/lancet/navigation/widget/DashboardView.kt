package cn.lancet.navigation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.px
import kotlin.math.cos
import kotlin.math.sin

const val OPEN_ANGLE = 120
val DASH_WIDTH = 2F.px
val DASH_HEIGHT = 10F.px
private val RADIUS = 150f.px
val DASH_LENGTH = 120f.px

class DashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mDashPath = Path().apply {
        addRect(0f, 0f, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CW)
    }

    private val mPath = Path()

    private val mPaintDash = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 3f.px
        style = Paint.Style.STROKE
    }

    private val mPaintArc = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 3f.px
        style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mPath.reset()
        mPath.addArc(
            width / 2f - RADIUS, height / 2f - RADIUS,
            width / 2f + RADIUS, height / 2f + RADIUS, 90 + OPEN_ANGLE / 2f,
            (360 - OPEN_ANGLE).toFloat()
        )
        val pathMeasure = PathMeasure(mPath, false)
        val phase = (pathMeasure.length - DASH_WIDTH) / 20
        mPaintDash.pathEffect = PathDashPathEffect(
            mDashPath, phase, 0F,
            PathDashPathEffect.Style.ROTATE
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath, mPaintArc)
        canvas.drawPath(mPath, mPaintDash)

        canvas.drawLine(
            width / 2f, height / 2f,
            (width / 2f + DASH_LENGTH * cos(
                markToRadians(5F)
            )).toFloat(),
            (height / 2f + DASH_LENGTH * sin(
                markToRadians(5F)
            )).toFloat(),
            mPaintArc
        )
    }
    private fun markToRadians(mark: Float): Double {
        return Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20F * mark).toDouble())
    }

}
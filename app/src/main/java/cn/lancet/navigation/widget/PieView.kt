package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.px
import kotlin.math.cos
import kotlin.math.sin

private val RADIUS = 150f.px
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(
    Color.parseColor("#C2185B"),
    Color.parseColor("#558B2F"),
    Color.parseColor("#00ACC1"),
    Color.parseColor("#5D4037")
)

private val OFFSET_LENGTH = 20f.px

class PieView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        mPath.addArc(
//            width / 2f - RADIUS, height / 2f - RADIUS,
//            width / 2f + RADIUS, height / 2f + RADIUS,
//            90 + OPEN_ANGLE / 2f,
//            (360 - OPEN_ANGLE).toFloat()
//        )
    }

    override fun onDraw(canvas: Canvas) {
        var startAngle = 0F
        ANGLES.forEachIndexed { index, angle ->
            mPaint.color = COLORS[index]
            if (index == 2) {
                canvas.save()
                canvas.translate(
                    (OFFSET_LENGTH * cos(Math.toRadians((startAngle + angle / 2F).toDouble()))).toFloat(),
                    (OFFSET_LENGTH * sin(Math.toRadians((startAngle + angle / 2F).toDouble()))).toFloat()
                )
            }

            canvas.drawArc(
                width / 2f - RADIUS, height / 2f - RADIUS,
                width / 2f + RADIUS, height / 2f + RADIUS,
                startAngle, angle, true, mPaint
            )
            startAngle += angle
            if (index == 2) {
                canvas.restore()
            }
        }
    }


}
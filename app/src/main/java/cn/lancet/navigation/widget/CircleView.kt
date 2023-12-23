package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.dp

class CircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var radius = 50.dp
        set(value) {
            field = value
            invalidate()
        }

    init {
        mPaint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2F, height / 2F, radius.toFloat(), mPaint)
    }

}
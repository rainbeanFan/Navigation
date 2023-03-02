package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.R
import cn.lancet.navigation.px

private val IMAGE_WIDTH = 200F.px
private val IMAGE_PADDING = 20F.px

class RoundImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint(ANTI_ALIAS_FLAG)

    private val xFermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    private lateinit var bounds: RectF
    private var mPath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bounds = RectF(
            width / 2 - IMAGE_WIDTH / 2, height / 2 - IMAGE_WIDTH / 2,
            width / 2 + IMAGE_WIDTH / 2,
            height / 2 + IMAGE_WIDTH / 2
        )
        mPath.reset()
        mPath.addArc(
            width / 2 - IMAGE_WIDTH / 2, height / 2 - IMAGE_WIDTH / 2,
            width / 2 + IMAGE_WIDTH / 2, height / 2 + IMAGE_WIDTH / 2,
            0f, 360f
        )
    }

    override fun onDraw(canvas: Canvas) {
        val saveLayerCount = canvas.saveLayer(bounds, null)
        canvas.drawOval(
            width / 2 - IMAGE_WIDTH / 2, height / 2 - IMAGE_WIDTH / 2,
            width / 2 + IMAGE_WIDTH / 2, height / 2 + IMAGE_WIDTH / 2, mPaint
        )
        mPaint.xfermode = xFermode
        canvas.drawBitmap(
            getImage(IMAGE_WIDTH.toInt()), width / 2 - IMAGE_WIDTH / 2,
            height / 2 - IMAGE_WIDTH / 2, mPaint
        )
        mPaint.xfermode = xFermode

        mPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 6F.px
        }
        canvas.drawPath(mPath, mPaint)
        canvas.restoreToCount(saveLayerCount)
    }

    private fun getImage(width: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, R.mipmap.splash, options)
        options.apply {
            inJustDecodeBounds = false
            inDensity = options.outWidth
            inTargetDensity = width
        }
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.splash, options)
        return Bitmap.createBitmap(
            bitmap, (bitmap.width / 2 - IMAGE_WIDTH / 2).toInt(),
            (bitmap.height / 2 - IMAGE_WIDTH / 2).toInt(),
            IMAGE_WIDTH.toInt(), IMAGE_WIDTH.toInt()
        )
    }


}
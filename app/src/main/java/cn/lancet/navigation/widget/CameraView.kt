package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.R
import cn.lancet.navigation.dp


private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

class CameraView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmap = getAvatar(BITMAP_SIZE.toInt())

    private val mCamera = Camera()

    private val mClipPath = Path().apply {
        addOval(
            BITMAP_PADDING, BITMAP_PADDING, BITMAP_PADDING + BITMAP_SIZE,
            BITMAP_PADDING + BITMAP_SIZE, Path.Direction.CW
        )
    }

    init {
        mCamera.rotateX(45F)
        mCamera.setLocation(0F, 0F, -3 * resources.displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.clipPath(mClipPath)
//        canvas.clipRect(BITMAP_PADDING,BITMAP_PADDING,BITMAP_PADDING+ BITMAP_SIZE/2,
//            BITMAP_PADDING+ BITMAP_SIZE/2)

        canvas.save()
        canvas.translate((BITMAP_PADDING + BITMAP_SIZE) / 2, (BITMAP_PADDING + mBitmap.height) / 2)
//        mCamera.applyToCanvas(canvas)
        canvas.clipRect(
            -BITMAP_PADDING / 2F, -mBitmap.height / 2F,
            BITMAP_SIZE / 2F, 0F
        )
        canvas.translate(
            -(BITMAP_PADDING + BITMAP_SIZE) / 2,
            -(BITMAP_PADDING + mBitmap.height) / 2
        )
        canvas.drawBitmap(mBitmap, BITMAP_PADDING, BITMAP_PADDING, mPaint)
        canvas.restore()
//  下半部分
        canvas.save()
        canvas.translate((BITMAP_PADDING + BITMAP_SIZE) / 2, (BITMAP_PADDING + mBitmap.height) / 2)
        mCamera.applyToCanvas(canvas)
        canvas.clipRect(
            -BITMAP_PADDING / 2F, 0F,
            BITMAP_SIZE / 2F, mBitmap.height / 2F
        )
        canvas.translate(
            -(BITMAP_PADDING + BITMAP_SIZE) / 2,
            -(BITMAP_PADDING + mBitmap.height) / 2
        )
        canvas.drawBitmap(mBitmap, BITMAP_PADDING, BITMAP_PADDING, mPaint)
        canvas.restore()
    }


    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.splash, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.splash, options)
    }

}
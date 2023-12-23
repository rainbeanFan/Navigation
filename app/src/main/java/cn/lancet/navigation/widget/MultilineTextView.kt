package cn.lancet.navigation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import cn.lancet.navigation.R
import cn.lancet.navigation.dp

private val IMAGE_MARGIN_TOP = 140.dp
private val IMAGE_WIDTH = 150.dp

class MultilineTextView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    val text =
        "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."

    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp.toFloat()
    }

    private val mFontMetrics = FontMetrics()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 18.dp.toFloat()
    }

    private val mBitmap = getImage(IMAGE_WIDTH.toInt())

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {


//        val staticLayout = StaticLayout(text,mPaint,width,Alignment.ALIGN_NORMAL,
//            1F,0F,false)
//
//
//        staticLayout.draw(canvas)


//        canvas.drawBitmap(mBitmap, width - IMAGE_WIDTH, IMAGE_MARGIN_TOP, mPaint)
        mPaint.getFontMetrics(mFontMetrics)
        val measuredWidth = floatArrayOf(0F)

        var start = 0
        var count: Int
        var verticalOffset = -mFontMetrics.top
        var maxWidth: Float
        while (start < text.length) {
            maxWidth = if (verticalOffset + mFontMetrics.bottom < IMAGE_MARGIN_TOP
                || verticalOffset + mFontMetrics.top >
                (IMAGE_MARGIN_TOP + mBitmap.height)
            ) {
                width.toFloat()
            } else ({
                (width - IMAGE_WIDTH)
            }).toFloat()

            count = mPaint.breakText(
                text, start, text.length,
                true, maxWidth, measuredWidth
            )

            canvas.drawText(
                text, start, start + count, 0F,
                verticalOffset, mPaint
            )
            start += count
            verticalOffset += mPaint.fontSpacing
        }

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
        return BitmapFactory.decodeResource(resources, R.mipmap.splash, options)
    }


}
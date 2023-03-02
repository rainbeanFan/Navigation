package cn.lancet.navigation.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import cn.lancet.navigation.R
import cn.lancet.navigation.dp


private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 8.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp


class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }
    private var floatingLabelShown = false
    private var floatingLabelFraction = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val animator by lazy {
        ObjectAnimator.ofFloat(
            this,
            "floatingLabelFraction",
            1F, 0F
        )
    }

    var mUseFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    setPadding(
                        paddingLeft,
                        (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingEnd, paddingBottom
                    )
                } else {
                    setPadding(
                        paddingLeft,
                        (paddingTop - TEXT_SIZE - TEXT_MARGIN).toInt(), paddingEnd, paddingBottom
                    )
                }
            }

        }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        mUseFloatingLabel = ta.getBoolean(R.styleable.MaterialEditText_useFloatingLabel,true)
        ta.recycle()
//        if (mUseFloatingLabel) {
//            setPadding(
//                paddingLeft,
//                (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingEnd, paddingBottom
//            )
//        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {

        if (text.isNullOrEmpty() && floatingLabelShown) {
            floatingLabelShown = false
            animator.start()
        } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
            floatingLabelShown = true
            animator.reverse()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mUseFloatingLabel){
            mPaint.apply {
                alpha = (floatingLabelFraction * 0xff).toInt()
            }
            val currentVerticalValue =
                VERTICAL_OFFSET + EXTRA_VERTICAL_OFFSET * (1 - floatingLabelFraction)
            canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, currentVerticalValue, mPaint)
        }


    }

}
package cn.lancet.navigation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class SideBar  : View {

    constructor(context: Context?):this(context,null,0)

    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int):super(context, attrs,defStyle)

    private var mListener:OnTouchingLetterChangedListener?=null
    private var choose = -1
    private var mPaint = Paint()
    private var mTextDialog:AppCompatTextView?=null

    fun setTextView(textDialog:AppCompatTextView?){
        mTextDialog = textDialog
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val height = height
        val width = width

        var singleHeight = (height * 1.0F)/ b.size
        singleHeight = (height * 1.0F - singleHeight/2)/ b.size
        for (i in b.indices){
            mPaint.apply {
                color = Color.parseColor("#434868")
                typeface = Typeface.DEFAULT_BOLD
                isAntiAlias = true
                textSize = 32F
            }

            if (i == choose){
                mPaint.apply {
                    color = Color.parseColor("#005EE9")
                    isFakeBoldText = true
                }
            }

            val xPos = width/2 - mPaint.measureText(b[i])/2
            val yPos = singleHeight * i + singleHeight
            canvas?.drawText(b[i],xPos,yPos,mPaint)
            mPaint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action
        val y = event.y
        val oldChoose = choose
        val listener = mListener
        val c = (y/height * b.size).toInt()

        when(action){
            MotionEvent.ACTION_UP -> {
                background = ColorDrawable(0x00000000)
                choose = -1
                invalidate()
                mTextDialog?.visibility = GONE
            }
            else -> {
                if (oldChoose!=c){
                    if (c>=0 && c< b.size){
                        mListener?.onTouchingLetterChanged(b[c])
                    }
                    mTextDialog?.let {
                        it.text = b[c]
                        it.visibility = VISIBLE
                    }

                    choose = c
                    invalidate()
                }
            }
        }
        return true
    }

    fun setOnTouchingLetterChangedListener(listener: OnTouchingLetterChangedListener){
        mListener = listener
    }

    interface OnTouchingLetterChangedListener {
        fun onTouchingLetterChanged(letter:String?)
    }

    companion object {
        var b = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
        )
    }

}
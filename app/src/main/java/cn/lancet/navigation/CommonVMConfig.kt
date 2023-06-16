package cn.lancet.navigation

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager

class CommonVMConfig(val context: Context,val view: View) {

    private var mWindowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var mWindowParams:WindowManager.LayoutParams = WindowManager.LayoutParams()

    init {
        configParams()
        show()
        handleBackKey()
    }

    private fun handleBackKey(){
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss()
                    return true
                }
                return false
            }
        })
    }

    private fun show(){
        mWindowManager.addView(view,mWindowParams)
    }

    private fun dismiss(){
        mWindowManager.removeView(view)
    }

    private fun configParams(){
        mWindowParams.apply {
            type = WindowManager.LayoutParams.TYPE_TOAST
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            gravity = Gravity.NO_GRAVITY
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

}
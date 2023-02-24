package cn.lancet.navigation.util

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager


class StatusBar {

    companion object {

        fun fitSystemBar(activity: Activity){
            val window = activity.window
            val decorView = window.decorView

            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT

        }

    }

}
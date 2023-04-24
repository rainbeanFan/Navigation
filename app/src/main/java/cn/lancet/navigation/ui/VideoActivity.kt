package cn.lancet.navigation.ui

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.navigation.R

class VideoActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun enterPIP(){
        val build = PictureInPictureParams.Builder()
            .build()
        enterPictureInPictureMode(build)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIP()
    }


























}
package cn.lancet.discovery

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import cn.lancet.common.ServiceFactory
import cn.lancet.find.R
import kotlinx.coroutines.flow.flow

class FindActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        findViewById<AppCompatTextView>(R.id.tv_find).setOnClickListener {
            ServiceFactory.instance.getUserInstallService()?.launch(this,"find")
        }




    }
    

}
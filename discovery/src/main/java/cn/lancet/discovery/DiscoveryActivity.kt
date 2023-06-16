package cn.lancet.discovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.common.ServiceFactory

class DiscoveryActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discovery)

        ServiceFactory.instance.getUserExitService()?.exit(this)

    }

}
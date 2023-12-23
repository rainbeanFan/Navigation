package cn.lancet.discovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.common.ServiceFactory
import cn.lancet.discovery.databinding.ActivityDiscoveryBinding

class DiscoveryActivity:AppCompatActivity() {

    private var mBinding:ActivityDiscoveryBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDiscoveryBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        ServiceFactory.instance.getUserExitService()?.exit(this)
    }



}
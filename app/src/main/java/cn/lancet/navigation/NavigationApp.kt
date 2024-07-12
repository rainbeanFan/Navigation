package cn.lancet.navigation

import android.app.Application
import cn.bmob.v3.Bmob
import cn.bmob.v3.ai.BmobAI
import cn.lancet.common.IAppComponent
import cn.lancet.common.ServiceFactory
import cn.lancet.discovery.FindService
import cn.lancet.discovery.UserInstallService
import com.hjq.toast.Toaster
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NavigationApp : Application(), IAppComponent {

    companion object {
        var mBmobAI: BmobAI? = null
    }

    override fun onCreate() {
        super.onCreate()

        initialize(this)

        Toaster.init(this)
        Bmob.initialize(this, "18f0791eb905bf4a3efb8769d449c9e9")
        mBmobAI = BmobAI()
        MMKV.initialize(this)

    }


    override fun initialize(app: Application) {

        ServiceFactory.instance.setUserInstallService(UserInstallService())
        ServiceFactory.instance.setUserExitService(FindService())

    }


}
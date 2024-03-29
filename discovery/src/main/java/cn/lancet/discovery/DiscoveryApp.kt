package cn.lancet.discovery

import android.app.Application
import cn.lancet.common.IAppComponent
import cn.lancet.common.ServiceFactory

class DiscoveryApp:Application(),IAppComponent {

    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }

    override fun initialize(app: Application) {
        ServiceFactory.instance.setUserInstallService(UserInstallService())
    }


}
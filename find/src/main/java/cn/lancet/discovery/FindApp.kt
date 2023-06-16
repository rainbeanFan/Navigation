package cn.lancet.discovery

import android.app.Application
import cn.lancet.common.IAppComponent
import cn.lancet.common.ServiceFactory

class FindApp:Application(),IAppComponent {

    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }

    override fun initialize(app: Application) {
        ServiceFactory.instance.setUserExitService(FindService())
    }


}
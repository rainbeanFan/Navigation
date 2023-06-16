package cn.lancet.navigation

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import cn.bmob.v3.Bmob
import cn.lancet.common.AppConfig
import cn.lancet.common.IAppComponent
import cn.lancet.common.ServiceFactory
import cn.lancet.discovery.FindService
import cn.lancet.discovery.UserInstallService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hjq.toast.Toaster
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@HiltAndroidApp
class NavigationApp:Application(),IAppComponent,DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        initialize(this)

        Toaster.init(this)
        Bmob.initialize(this,"18f0791eb905bf4a3efb8769d449c9e9")
//        RongIM.init(this,"k51hidwqkv3tb")
        MMKV.initialize(this)
    }


    override fun onStop(owner: LifecycleOwner) {
//        Toaster.show("onStop")
    }

    override fun onPause(owner: LifecycleOwner) {
        Toaster.show("onPause")
    }

    override fun onResume(owner: LifecycleOwner) {
        Toaster.show("onResume")
    }

    private fun initStores() {
        val storage = Firebase.storage("")
        storage.reference
    }

    private fun getLancetProcessName():String?{
        try {
            val file = File("/proc/"+android.os.Process.myPid()+"/"+"cmdline")
            val mBufferedReader = BufferedReader(FileReader(file))
            val processName = mBufferedReader.readLine().trim()
            mBufferedReader.close()
            return processName
        }catch (e:Exception){
            return null
        }
    }

    override fun initialize(app: Application) {

        ServiceFactory.instance.setUserInstallService(UserInstallService())
        ServiceFactory.instance.setUserExitService(FindService())

        AppConfig.COMPONENTS.forEach {component->
            try {
                val clazz = Class.forName(component)
                val obj = clazz.newInstance()
                if (obj is IAppComponent){
                    obj.initialize(app)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


}
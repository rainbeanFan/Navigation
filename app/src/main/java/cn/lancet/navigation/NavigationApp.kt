package cn.lancet.navigation

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import cn.bmob.v3.Bmob
import cn.bmob.v3.ai.BmobAI
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
import io.rong.imkit.IMCenter
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.InitOption
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@HiltAndroidApp
class NavigationApp:Application(),IAppComponent {

    companion object{
        var mBmobAI:BmobAI?=null
    }

    override fun onCreate() {
        super.onCreate()

        initialize(this)

        Toaster.init(this)
        Bmob.initialize(this,"18f0791eb905bf4a3efb8769d449c9e9")
        mBmobAI = BmobAI()
//        RongIM.init(this,"k51hidwqkv3tb")
        MMKV.initialize(this)

        //初始化融云
        IMCenter.init(this,"k51hidwqkv3tb", InitOption.Builder().build())

        IMCenter.getInstance().connect("edf8qUE/9KvhXrEtY67XDK+ti5xB+qFa64GkmNtZslE=@osi5.cn.rongnav.com;osi5.cn.rongcfg.com",0,object : RongIMClient.ConnectCallback(){
            override fun onSuccess(userId: String?) {
                Toaster.show("连接成功")
            }

            override fun onError(e: RongIMClient.ConnectionErrorCode?) {
                if (e == RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT) {
                    Toaster.show("token错误")
                }else if (e == RongIMClient.ConnectionErrorCode.RC_CONNECT_TIMEOUT){
                    Toaster.show("连接超时")
                }
            }

            override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
                if (code == RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS){
                    Toaster.show("数据库打开成功")
                }else{
                    Toaster.show("数据库打开失败")
                }
            }
        })

    }


    override fun initialize(app: Application) {

        ServiceFactory.instance.setUserInstallService(UserInstallService())
        ServiceFactory.instance.setUserExitService(FindService())

    }


}
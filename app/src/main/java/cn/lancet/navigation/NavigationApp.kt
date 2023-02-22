package cn.lancet.navigation

import android.app.Application
import cn.bmob.v3.Bmob
import com.hjq.toast.Toaster
import com.tencent.mmkv.MMKV
import io.rong.imkit.RongIM

class NavigationApp:Application() {

    override fun onCreate() {
        super.onCreate()

        Toaster.init(this)

        Bmob.initialize(this,"18f0791eb905bf4a3efb8769d449c9e9")

        RongIM.init(this,"k51hidapi3tb")

        MMKV.initialize(this)

    }

}
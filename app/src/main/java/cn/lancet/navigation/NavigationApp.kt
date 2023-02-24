package cn.lancet.navigation

import android.app.Application
import cn.bmob.v3.Bmob
import com.hjq.toast.Toaster
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import io.rong.imkit.RongIM
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@HiltAndroidApp
class NavigationApp:Application() {

    override fun onCreate() {
        super.onCreate()

        Toaster.init(this)

        Bmob.initialize(this,"18f0791eb905bf4a3efb8769d449c9e9")

        RongIM.init(this,"k51hidwqkv3tb")

        MMKV.initialize(this)

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


}
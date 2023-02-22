package cn.lancet.navigation.util

import java.text.SimpleDateFormat

class TimeUtil {

    companion object {

        @JvmStatic
        fun ctsToTimeStr(timestamp: Long): String{
            var time = ""
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            time = format.format(timestamp)
            return time
        }

    }

}
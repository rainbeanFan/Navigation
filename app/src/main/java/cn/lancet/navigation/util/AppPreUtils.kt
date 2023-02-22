package cn.lancet.navigation.util

import com.tencent.mmkv.MMKV

class AppPreUtils {

    companion object{

        fun putString(key: String, value: String){
            MMKV.defaultMMKV().putString(key, value)
        }

        fun getString(key: String): String{
            return MMKV.defaultMMKV().getString(key,"")?:""
        }

        fun clearAll() {
            MMKV.defaultMMKV().clearAll()
        }
    }

}
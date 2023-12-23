package cn.lancet.navigation.singleton

class UserManager02 private constructor(name:String){

    companion object {
        @Volatile
        private var INSTANCE:UserManager02? = null
        @JvmStatic
        fun getInstance(name: String):UserManager02 = INSTANCE ?: synchronized(this){

            INSTANCE?:UserManager02(name).also { INSTANCE = it }
        }
    }

}
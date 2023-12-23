package cn.lancet.navigation.singleton

class UserSingleton private constructor(name:String){

    companion object {
        @JvmStatic
        fun create(name: String):UserSingleton?{
            return UserSingleton(name)
        }
    }

}
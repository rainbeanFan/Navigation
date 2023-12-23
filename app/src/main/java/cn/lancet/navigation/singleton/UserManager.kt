package cn.lancet.navigation.singleton

class UserManager private constructor(name:String){

    companion object:BaseSingleton<String,UserManager>() {
        override fun creator(params: String) =  UserManager(params)

    }
}
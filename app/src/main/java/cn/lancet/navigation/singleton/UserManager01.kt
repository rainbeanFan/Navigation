package cn.lancet.navigation.singleton

object UserManager01 {

    val userSingleton by lazy { loadUser() }

    private fun loadUser():UserSingleton{

        return UserSingleton.create("TONY")!!
    }

    fun login(){}

}
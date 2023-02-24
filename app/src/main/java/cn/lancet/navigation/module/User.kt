package cn.lancet.navigation.module

import cn.bmob.v3.BmobObject

data class User(
    var sort_letter: String?=null,
    val account:String?=null,
    val RCToken:String?=null,
    val pwd:String?=null,
    val name: String?=null,
    val avatar: String?=null,
    val email: String?=null
) : BmobObject() {

    override fun toString(): String {
        return "User(account=$account, pwd=$pwd, name='$name', avatar='$avatar', email='$email')"
    }
}

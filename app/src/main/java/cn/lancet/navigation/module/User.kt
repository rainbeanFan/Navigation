package cn.lancet.navigation.module

import cn.bmob.v3.BmobUser

data class User(
    var sort_letter: String?=null,
    val account:String?=null,
    val RCToken:String?=null,
    val pwd:String?=null,
    val name: String?=null,
    var avatar: String?=null
) : BmobUser()
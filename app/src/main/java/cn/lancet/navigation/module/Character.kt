package cn.lancet.navigation.module

import cn.bmob.v3.BmobObject

data class Character(
    var userNum:String?=null,
    var cnName:String,
    var enName:String,
    var avatar:String?=null,
    var prompt:String,
    var description:String?=null,
    var welTips:String?=null,
    var rank:Int
):BmobObject(){
    override fun toString(): String {
        return "Character(userNum=$userNum, cnName='$cnName', enName='$enName', avatar=$avatar, prompt='$prompt', description=$description, welTips=$welTips, rank=$rank)"
    }
}


const val SEND_BY_ME = "me"
const val SEND_BY_BOT = "bot"

data class BmobMessage(
    val session:String,
    val userName:String,
    val message:String,
    val sendBy:String,
):BmobObject(){
    override fun toString(): String {
        return "BmobMessage(session='$session', userName='$userName', message='$message', sendBy='$sendBy')"
    }
}

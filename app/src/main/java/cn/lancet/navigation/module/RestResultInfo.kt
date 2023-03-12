package cn.lancet.navigation.module

import cn.bmob.v3.BmobObject


data class RestResultInfo(
    var userId: String?=null,
    var favourite:Boolean?=null,
    var plantName: String?=null,
    var plantUrl: String?=null,
    var plantDescription: String?=null
):BmobObject(){

}

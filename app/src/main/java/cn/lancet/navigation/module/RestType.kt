package cn.lancet.navigation.module

data class RestType(
    var restId:Int? = null,
    var name:String?=null,
    var url:String?=null,
    var type:Int = 0,
    val active:String?=null
)

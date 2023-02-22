package cn.lancet.navigation.module

import cn.bmob.v3.BmobObject

data class Notice(
    var noticeId: String?=null,
    val createdTime: Long?=null,
    val updatedTime: Long?=null,
    val coverImageUrl: String?=null,
    val noticeTitle: String?=null,
    val noticeContent: String?=null,
    val publisher: String?=null
):BmobObject(){
    override fun toString(): String {
        return "Notice(createdTime=$createdTime, updatedTime=$updatedTime, coverImageUrl=$coverImageUrl, noticeTitle=$noticeTitle, noticeContent=$noticeContent, publisher=$publisher)"
    }
}

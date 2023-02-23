package cn.lancet.navigation.database

import cn.lancet.navigation.module.Notice
import kotlinx.coroutines.flow.Flow


interface NoticeModelDao {
    fun getNotices():Flow<List<Notice>>
    suspend fun addNotice(note: Notice)
    suspend fun deleteNotice(noteId: String)

}

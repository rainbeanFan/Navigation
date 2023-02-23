package cn.lancet.navigation.repository

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import cn.lancet.navigation.database.NoticeModelDao
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.NoticesRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class NoticeModelRepository @Inject constructor() {

    suspend fun getNotices() {

        val queries = BmobQuery<Notice>()

        var flowNotice:Flow<MutableList<Notice>>?=null

        val bqlString = "select * from Notice order by -createdTime"

        queries.doSQLQuery(bqlString, object : SQLQueryListener<Notice>() {
            override fun done(notices: BmobQueryResult<Notice>?, e: BmobException?) {
                if (notices != null){
                    flowNotice = flow {
                        emit(notices.results)
                    }.onCompletion {

                    }
                }
            }
        })
//        noticeModelDao.getNotices()
    }

    suspend fun addNotice(notice: Notice) {
//        noticeModelDao.addNotice(notice)
    }

    suspend fun deleteNotice(noticeId: String) {
//        noticeModelDao.deleteNotice(noticeId)
    }

    companion object {

        @Volatile
        private var instance:NoticeModelRepository?=null

        fun getInstance() =
            instance?: synchronized(this){
                instance?: NoticeModelRepository().also { instance = it }
            }

    }

}
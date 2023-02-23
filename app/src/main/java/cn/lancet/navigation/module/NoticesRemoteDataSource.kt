package cn.lancet.navigation.module

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NoticesRemoteDataSource(
    private val noticesApi: NoticesApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchLatestNotices():List<Notice> =
        withContext(ioDispatcher){
            noticesApi.fetchLatestNotices()
        }

}


interface NoticesApi {
    fun fetchLatestNotices():List<Notice>
}

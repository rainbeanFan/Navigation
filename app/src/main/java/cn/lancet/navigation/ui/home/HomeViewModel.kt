package cn.lancet.navigation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SQLQueryListener
import cn.lancet.navigation.module.Comment
import cn.lancet.navigation.module.Notice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class NoticeListViewModel :ViewModel() {

    var noticeStateFlow = MutableSharedFlow<MutableList<Notice>>()
    var commentStateFlow = MutableSharedFlow<Pair<String,MutableList<Comment>>>()

    var mNotice:MutableList<Notice>? = null

    fun getNoticeList(){
        viewModelScope.launch {
            val queries = BmobQuery<Notice>()
            val bqlString = "select * from Notice order by -createdTime"
            queries.doSQLQuery(bqlString, object : SQLQueryListener<Notice>() {
                override fun done(notices: BmobQueryResult<Notice>?, e: BmobException?) {
                    mNotice = notices?.results

                    viewModelScope.launch {
                        if (notices != null){
                            noticeStateFlow.emit(notices.results)
                        }
                    }
                }
            })
        }
    }


    fun getComment(noticeId:String){
        val query = BmobQuery<Comment>()
        val bmobQuery = query.addWhereEqualTo("noticeId", noticeId)
        bmobQuery.findObjects(object : FindListener<Comment>() {
            override fun done(comments: MutableList<Comment>?, e: BmobException?) {
                comments?.let { comments
                    viewModelScope.launch {
                        commentStateFlow.emit(Pair(noticeId,comments))
                    }
                }
            }
        })
    }

}

sealed interface ModelUiState {
    object Loading: ModelUiState
    data class Error(val throwable: Throwable): ModelUiState
    data class Success(val data:List<Notice>): ModelUiState
}
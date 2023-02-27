package cn.lancet.navigation.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.lancet.navigation.module.Notice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class NoticeViewModel ():ViewModel() {

    var noticeFlow = MutableSharedFlow<Notice>()
    var deleteFlow = MutableSharedFlow<Boolean>()
    var addFlow = MutableSharedFlow<Notice>()
    fun getNoticeDetail(noticeId:String){
        val queries = BmobQuery<Notice>()
        queries.getObject(noticeId, object : QueryListener<Notice>() {
            override fun done(notice: Notice?, e: BmobException?) {
                if (e == null) {
                    viewModelScope.launch {
                        notice?.let {
                            noticeFlow.emit(it)
                        }
                    }
                }
            }
        })
    }


    fun addNotice(notice: Notice){
        notice.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null && objectId!=null) {
                    viewModelScope.launch {
                        addFlow.emit(notice)
                    }
                }
            }
        })
    }

    fun deleteNotice(noticeId:String){
        val notice = Notice()
        notice.delete(noticeId, object : UpdateListener() {
            override fun done(e: BmobException?) {
                viewModelScope.launch {
                    deleteFlow.emit(e == null)
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
package cn.lancet.navigation.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.lancet.navigation.module.Notice
import kotlinx.coroutines.launch



class NoticeViewModel ():ViewModel() {

    var noticeLiveData = MutableLiveData<Notice>()
    var deleteNotice = MutableLiveData<Boolean>()
    var addNotice = MutableLiveData<Notice>()
    fun getNoticeDetail(noticeId:String){
        viewModelScope.launch {
            val queries = BmobQuery<Notice>()

            queries.getObject(noticeId, object : QueryListener<Notice>() {
                override fun done(notice: Notice?, e: BmobException?) {
                    if (e == null) {
                        notice?.let {
                            noticeLiveData.postValue(it)
                        }
                    }
                }
            })
        }
    }

    fun addNotice(notice: Notice){
        viewModelScope.launch{
            notice.save(object : SaveListener<String>() {
                override fun done(objectId: String?, e: BmobException?) {
                    if (e == null && objectId!=null) {
                        addNotice.postValue(notice)
                    }
                }
            })
        }
    }

    fun deleteNotice(noticeId:String){
        viewModelScope.launch {
            val notice = Notice()
            notice.delete(noticeId, object : UpdateListener() {
                override fun done(e: BmobException?) {
                    deleteNotice.postValue(e == null)
                }
            })
        }
    }

}

sealed interface ModelUiState {
    object Loading: ModelUiState
    data class Error(val throwable: Throwable): ModelUiState
    data class Success(val data:List<Notice>): ModelUiState
}
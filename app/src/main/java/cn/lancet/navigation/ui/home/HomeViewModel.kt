package cn.lancet.navigation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.repository.NoticeModelRepository
import kotlinx.coroutines.launch


class NoticeListViewModel ():ViewModel() {

    var _notices = MutableLiveData<MutableList<Notice>>()
    fun getNoticeList(){
        viewModelScope.launch {
            val queries = BmobQuery<Notice>()
            val bqlString = "select * from Notice order by -createdTime"
            queries.doSQLQuery(bqlString, object : SQLQueryListener<Notice>() {
                override fun done(notices: BmobQueryResult<Notice>?, e: BmobException?) {
                    if (notices != null){
                        _notices.postValue(notices.results)
                    }
                }
            })
        }
    }

    fun addNotice(notice: Notice){
        viewModelScope.launch{
            NoticeModelRepository.getInstance().addNotice(notice)
        }
    }

    fun deleteNotice(noticeId:String){
        viewModelScope.launch {
            NoticeModelRepository.getInstance().deleteNotice(noticeId)
        }
    }

}

sealed interface ModelUiState {
    object Loading: ModelUiState
    data class Error(val throwable: Throwable): ModelUiState
    data class Success(val data:List<Notice>): ModelUiState
}
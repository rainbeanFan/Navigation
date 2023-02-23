package cn.lancet.navigation.ui.me

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.R
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.User
import cn.lancet.navigation.repository.NoticeModelRepository
import cn.lancet.navigation.util.AppPreUtils
import coil.load
import kotlinx.coroutines.launch
import java.util.Locale


class MeViewModel():ViewModel() {

    var userLiveData = MutableLiveData<User>()
    fun getUserInfo(){
        viewModelScope.launch {

            val queries = BmobQuery<User>()

            queries.getObject(AppPreUtils.getString(Constant.KEY_USER_ID), object : QueryListener<User>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null) {
                        user?.let {
                            userLiveData.postValue(it)
                        }
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


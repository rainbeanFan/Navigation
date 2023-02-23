package cn.lancet.navigation.ui.me

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils
import kotlinx.coroutines.launch


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

}


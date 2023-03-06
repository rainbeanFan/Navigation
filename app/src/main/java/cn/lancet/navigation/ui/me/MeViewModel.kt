package cn.lancet.navigation.ui.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class MeViewModel() : ViewModel() {

    val sharedFlow = MutableSharedFlow<User>()



    fun getUserInfo() {

        BmobUser.fetchUserInfo(object : FetchUserInfoListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    user?.let {
                        viewModelScope.launch {
                            sharedFlow.emit(it)
                        }
                    }
                }
            }

        })

    }

}


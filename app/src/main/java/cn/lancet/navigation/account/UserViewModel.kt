package cn.lancet.navigation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.module.User
import cn.lancet.navigation.ui.me.UserAvatar
import com.hjq.toast.Toaster
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val loginSharedFlow = MutableSharedFlow<User?>()
    val signupSharedFlow = MutableSharedFlow<Boolean>()
    val userAvatarFlow = MutableSharedFlow<UserAvatar>()

    fun login(account: String, pwd: String) {

        val user = User().apply {
            username = account
            setPassword(pwd)
        }
        user.login(object : SaveListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                viewModelScope.launch {
                    if (e == null) {
                        loginSharedFlow.emit(user)
                    } else {
                        if (e.errorCode == 101) {
                            Toaster.show("用户名或密码错误")
                        } else {
                            Toaster.show("${e.message}")
                        }
                    }
                }
            }
        })
    }

    fun signUp(account: String, password: String, avatar: String) {

        val user = User(account = account, name = account, avatar = avatar).apply {
            username = account
            setPassword(password)
            email = account
        }

        user.signUp(object : SaveListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                viewModelScope.launch {
                    signupSharedFlow.emit(e == null && !user?.objectId.isNullOrBlank())
                }
            }
        })
    }

    fun modifyAvatar(path: String) {

    }


}
package cn.lancet.navigation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.lancet.navigation.module.User
import cn.lancet.navigation.ui.me.UserAvatar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val loginSharedFlow = MutableSharedFlow<User?>()
    val signupSharedFlow = MutableSharedFlow<Boolean>()
    val userAvatarFlow = MutableSharedFlow<UserAvatar>()

    fun login(account: String, pwd: String) {
        viewModelScope.launch {
            loginSharedFlow.emit(User(account = account, name = account.ifBlank { "guest" }))
        }
    }

    fun signUp(account: String, password: String, avatar: String) {
        viewModelScope.launch {
            signupSharedFlow.emit(true)
        }
    }

    fun modifyAvatar(path: String) {

    }


}

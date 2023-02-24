package cn.lancet.navigation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils
import com.hjq.toast.Toaster
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {

    val loginSharedFlow = MutableSharedFlow<User?>()
    val signupSharedFlow = MutableSharedFlow<Boolean>()

    fun login(account:String,pwd:String) {

        val userBmobQuery = BmobQuery<User>().addWhereEqualTo("account", account)
        userBmobQuery.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null){
                    val user = users?.get(0)
                    if (user?.pwd.equals(pwd)){
                        user?.objectId?.let {
                            AppPreUtils.putString(Constant.KEY_USER_ID, it)
                        }

                        user?.avatar?.let {
                            AppPreUtils.putString(Constant.KEY_AVATAR,it)
                        }
                        viewModelScope.launch {
                            loginSharedFlow.emit(user)
                        }
                    }else{
                        Toaster.showLong("The account and password don`t match.")
                    }
                }else{
                    signUp(account,pwd)
                }
            }

        })

    }

    fun signUp(account:String,password:String){
        val user = User(account = account, pwd = password,name = account)

        user.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                viewModelScope.launch {
                    signupSharedFlow.emit(e == null && !objectId.isNullOrBlank())
                }
            }
        })
    }

}
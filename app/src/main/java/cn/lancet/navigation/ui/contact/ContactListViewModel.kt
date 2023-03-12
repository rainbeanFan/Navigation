package cn.lancet.navigation.ui.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.module.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale


class ContactListViewModel() : ViewModel() {

    var contactSharedFlow = MutableSharedFlow<MutableList<User>>()
    fun getContactList() {
        val queries = BmobQuery<User>()
        queries.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null && users != null) {
                    val result = users.filter {
                        it.objectId != BmobUser.getCurrentUser(User::class.java).objectId
                    }

                    viewModelScope.launch {
                        contactSharedFlow.emit(result.toMutableList())
                    }
                }
            }
        })

    }

}


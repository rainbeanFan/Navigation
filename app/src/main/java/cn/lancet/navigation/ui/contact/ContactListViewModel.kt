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
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ContactListViewModel() : ViewModel() {

    var contactSharedFlow = MutableSharedFlow<MutableList<User>>()
    suspend fun getContactList() = suspendCancellableCoroutine { continuation ->
        val queries = BmobQuery<User>()
        queries.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null && users != null) {
                    val result = users.filter {
                        it.objectId != BmobUser.getCurrentUser(User::class.java).objectId
                    }
                    result.forEach {
                        it.sort_letter = it.name?.uppercase(Locale.ROOT)?.get(0).toString()
                    }
                    continuation.resume(result.toMutableList())
//                    viewModelScope.launch {
//                        contactSharedFlow.emit(result.toMutableList())
//                    }
                }else{
                    continuation.resumeWithException(BmobException(e))
                }
            }
        })

    }

}


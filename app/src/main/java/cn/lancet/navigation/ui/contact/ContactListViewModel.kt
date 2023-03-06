package cn.lancet.navigation.ui.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.module.User
import com.mayabot.nlp.module.pinyin.Pinyins
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
                    result.forEach {
                        val pinyin = Pinyins.convert(it.name)
                        it.sort_letter =
                            if (pinyin!!.asString().substring(0, 1).uppercase(Locale.getDefault())
                                    .matches("[A-Z]".toRegex())
                            ) {
                                pinyin.asString().substring(0, 1).uppercase(Locale.getDefault())
                            } else {
                                "#"
                            }
                    }

                    viewModelScope.launch {
                        contactSharedFlow.emit(result.toMutableList())
                    }
                }
            }
        })

    }

}


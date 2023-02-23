package cn.lancet.navigation.ui.contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.module.User
import kotlinx.coroutines.launch
import java.util.Locale


class ContactListViewModel():ViewModel() {

    var contacts = MutableLiveData<MutableList<User>>()
    fun getContactList(){
        viewModelScope.launch {

            val queries = BmobQuery<User>()

            queries.findObjects(object : FindListener<User>() {
                override fun done(users: MutableList<User>?, e: BmobException?) {
                    if (e == null && users != null) {

                        users.forEach {
                            it.sort_letter =
                                if (it.name!!.substring(0,1).uppercase(Locale.getDefault()).matches("[A-Z]".toRegex())){
                                    it.name.substring(0,1).uppercase(Locale.getDefault())
                                }else{
                                    "#"
                                }
                        }
                        contacts.postValue(users!!)
                    }
                }
            })
        }
    }

}


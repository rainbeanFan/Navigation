package cn.lancet.navigation.ui.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.lancet.navigation.module.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Timer
import java.util.TimerTask
import kotlin.coroutines.resumeWithException


class MeViewModel() : ViewModel() {

    private val _stateFlow = MutableStateFlow(0)

    val stateFlow = _stateFlow.asStateFlow()

    fun startTimer() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                _stateFlow.value += 1
            }
        }, 0, 1000)
    }

    private val timeFlow = flow {
        var time = 0
        while (true) {
            emit(time)
            delay(1000)
            time++
        }
    }

    val stateFlow2 =
        timeFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    suspend fun getUserInfo(): User =

        suspendCancellableCoroutine { continuation ->
            BmobUser.fetchUserInfo(object : FetchUserInfoListener<User>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null && user != null) {
                        continuation.resumeWith(Result.success(user))
                    } else {
                        continuation.resumeWithException(BmobException(e))
                    }
                }

            })
        }


}


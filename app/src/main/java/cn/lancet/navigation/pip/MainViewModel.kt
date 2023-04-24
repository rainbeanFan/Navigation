package cn.lancet.navigation.pip

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private var job: Job? = null

    private var startUptimeMillis = SystemClock.uptimeMillis()
    private val timeMillis = MutableLiveData(0L)

    private val _started = MutableLiveData(false)

    val started:LiveData<Boolean> = _started
    val time = timeMillis.map {millis ->
        val minutes = millis/1000/60
        val m = minutes.toString().padStart(2,'0')
        val seconds = (millis/1000) % 60
        val s = seconds.toString().padStart(2,'0')
        val hundredths = (millis%1000)/10
        val h = hundredths.toString().padStart(2,'0')
        "$m:$s:$h"
    }

    fun startOrPause(){
        if (_started.value == true){
            _started.value = false
            job?.cancel()
        }else{
            _started.value = true
            job = viewModelScope.launch { start() }
        }
    }

    private suspend fun CoroutineScope.start(){
        startUptimeMillis = SystemClock.uptimeMillis() - (timeMillis.value?:0L)
        while (isActive){
            timeMillis.value = SystemClock.uptimeMillis() - startUptimeMillis
//            updates on every render frame
            awaitFrame()
        }
    }

    fun clear(){
        startUptimeMillis = SystemClock.uptimeMillis()
        timeMillis.value = 0L
    }

}
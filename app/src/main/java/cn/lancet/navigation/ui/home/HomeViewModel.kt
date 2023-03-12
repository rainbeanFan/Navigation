package cn.lancet.navigation.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.db.DBManager
import cn.lancet.navigation.db.RestResultEntity
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.RestResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class PlantListViewModel : ViewModel() {

    var mPlantInfoFlow = MutableSharedFlow<MutableList<RestResultInfo>>()

    var mLocalRestInfoFlow = MutableSharedFlow<MutableList<RestResultEntity>>()

    fun getPlantInfo(userId: String) {
        val query = BmobQuery<RestResultInfo>()
        val bmobQuery = query.addWhereEqualTo("userId", userId)
        bmobQuery.findObjects(object : FindListener<RestResultInfo>() {
            override fun done(plants: MutableList<RestResultInfo>?, e: BmobException?) {
                if (e == null) {
                    plants?.let {
                        viewModelScope.launch {
                            mPlantInfoFlow.emit(plants.filter {
                                it.plantDescription != null
                            }.toMutableList())
                        }
                    }
                }
            }
        })
    }

    fun getLocalRestInfo(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val allRest = DBManager.instance.getDB(context).restResultDao().getAllRest()
            mLocalRestInfoFlow.emit(allRest)
        }
    }

}

sealed interface ModelUiState {
    object Loading : ModelUiState
    data class Error(val throwable: Throwable) : ModelUiState
    data class Success(val data: List<Notice>) : ModelUiState
}
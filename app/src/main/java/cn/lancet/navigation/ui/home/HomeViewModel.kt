package cn.lancet.navigation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.PlantDiscoveryInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class PlantListViewModel : ViewModel() {

    var mPlantInfoFlow = MutableSharedFlow<MutableList<PlantDiscoveryInfo>>()

    fun getPlantInfo(userId: String) {
        val query = BmobQuery<PlantDiscoveryInfo>()
        val bmobQuery = query.addWhereEqualTo("userId", userId)
        bmobQuery.findObjects(object : FindListener<PlantDiscoveryInfo>() {
            override fun done(plants: MutableList<PlantDiscoveryInfo>?, e: BmobException?) {
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

}

sealed interface ModelUiState {
    object Loading : ModelUiState
    data class Error(val throwable: Throwable) : ModelUiState
    data class Success(val data: List<Notice>) : ModelUiState
}
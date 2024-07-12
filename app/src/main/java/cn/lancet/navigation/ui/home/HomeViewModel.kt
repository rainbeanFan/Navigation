package cn.lancet.navigation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.module.Character
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class PlantListViewModel : ViewModel() {


    var mCharacterInfoFlow = MutableSharedFlow<MutableList<Character>>()

    fun getCharacterInfo() {
        val query = BmobQuery<Character>()
        query.order("rank").findObjects(object : FindListener<Character>() {
            override fun done(characters: MutableList<Character>?, e: BmobException?) {
                if (e == null) {
                    characters?.let {
                        viewModelScope.launch {
                            mCharacterInfoFlow.emit(characters)
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
    data class Success(val data: MutableList<Character>) : ModelUiState
}
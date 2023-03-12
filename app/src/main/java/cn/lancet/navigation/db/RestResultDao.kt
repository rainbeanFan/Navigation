package cn.lancet.navigation.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestResultDao {

    @Query("SELECT * FROM rest")
    fun getAllRest():MutableList<RestResultEntity>

//    @Query("SELECT * FROM rest WHERE restType = 'type'")
//    fun getRestByType(type:String):MutableList<RestResultEntity>

    @Insert
    fun insertRest(rest:RestResultEntity)

}
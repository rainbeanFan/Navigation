package cn.lancet.navigation.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "rest")
data class RestResultEntity(
    @PrimaryKey
    val restId: String,
    val restType: Int,
    var userId: String?=null,
    var favourite:Boolean?=null,
    var plantName: String?=null,
    var plantUrl: String?=null,
    var plantDescription: String?=null
){
    override fun toString(): String {
        return "RestResultEntity(restId='$restId', userId=$userId, favourite=$favourite, plantName=$plantName, plantUrl=$plantUrl, plantDescription=$plantDescription)"
    }

}

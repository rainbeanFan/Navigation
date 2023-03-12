package cn.lancet.navigation.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestResultEntity::class], version = 1)
abstract class LancetDatabase:RoomDatabase() {

    abstract fun restResultDao():RestResultDao

}
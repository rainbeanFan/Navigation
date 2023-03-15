package cn.lancet.navigation.db

import android.content.Context
import androidx.room.Room

class DBManager private constructor() {

    fun getDB(context: Context): LancetDatabase {
        val db = Room.databaseBuilder(context, LancetDatabase::class.java, "lancet-db").build()
        return db
    }

    companion object {
        val instance: DBManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DBManager()
        }

    }

}
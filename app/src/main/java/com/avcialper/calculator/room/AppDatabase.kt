package com.avcialper.calculator.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): HistoryDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun databaseConnect(context: Context): AppDatabase? {
            var _instance = INSTANCE
            if (_instance == null) {
                synchronized(this) {
                    _instance = Room.databaseBuilder(
                        context = context,
                        klass = AppDatabase::class.java,
                        name = "history"
                    ).build()
                }
            }
            INSTANCE = _instance
            return INSTANCE
        }

    }

}
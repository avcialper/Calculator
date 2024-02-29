package com.avcialper.calculator.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    suspend fun getAll(): List<History>

    @Insert
    suspend fun addValue(history: History)

    @Query("DELETE FROM history")
    suspend fun delete()

}
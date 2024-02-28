package com.avcialper.calculator.di

import android.content.Context
import com.avcialper.calculator.room.AppDatabase
import com.avcialper.calculator.room.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {
    @Singleton
    @Provides
    fun provideDao(@ApplicationContext context: Context) : HistoryDao {
        return AppDatabase.databaseConnect(context)!!.getDao()
    }
}
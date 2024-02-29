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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.databaseConnect(context)!!
    }

    @Singleton
    @Provides
    fun provideDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.getDao()
    }
}
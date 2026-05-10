package com.crewcomms.core.database

import android.content.Context
import androidx.room.Room
import com.crewcomms.core.database.dao.CrewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideCrewDatabase(@ApplicationContext context: Context): CrewDatabase =
        Room.databaseBuilder(context, CrewDatabase::class.java, "crewcomms.db").build()

    @Provides
    fun provideCrewDao(database: CrewDatabase): CrewDao = database.crewDao()
}

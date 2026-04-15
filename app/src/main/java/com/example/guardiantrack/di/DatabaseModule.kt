package com.example.guardiantrack.di

import android.content.Context
import androidx.room.Room
import com.example.guardiantrack.data.local.db.GuardianDatabase
import com.example.guardiantrack.data.local.db.dao.EmergencyContactDao
import com.example.guardiantrack.data.local.db.dao.IncidentDao
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
    fun provideDatabase(@ApplicationContext context: Context): GuardianDatabase =
        Room.databaseBuilder(context, GuardianDatabase::class.java, GuardianDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideIncidentDao(db: GuardianDatabase): IncidentDao = db.incidentDao()

    @Provides
    fun provideContactDao(db: GuardianDatabase): EmergencyContactDao = db.contactDao()
}
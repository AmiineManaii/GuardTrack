package com.example.guardiantrack.di

import com.example.guardiantrack.data.local.db.GuardianDatabase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ContentProviderEntryPoint {
    fun guardianDatabase(): GuardianDatabase
}
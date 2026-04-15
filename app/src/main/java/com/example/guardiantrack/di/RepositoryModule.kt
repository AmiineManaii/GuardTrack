package com.example.guardiantrack.di

import com.example.guardiantrack.data.repository.ContactRepositoryImpl
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.repository.ContactRepository
import com.example.guardiantrack.domain.repository.IncidentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIncidentRepository(impl: IncidentRepositoryImpl): IncidentRepository

    @Binds
    @Singleton
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository
}
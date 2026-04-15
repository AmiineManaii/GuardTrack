package com.example.guardiantrack.domain.repository

import com.example.guardiantrack.domain.model.EmergencyContact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<EmergencyContact>>
    suspend fun insertContact(contact: EmergencyContact)
    suspend fun deleteContact(id: Long)
}
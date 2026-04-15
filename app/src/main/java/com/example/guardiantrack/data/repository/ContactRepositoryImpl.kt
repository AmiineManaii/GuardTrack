package com.example.guardiantrack.data.repository

import com.example.guardiantrack.data.local.db.dao.EmergencyContactDao
import com.example.guardiantrack.domain.model.EmergencyContact
import com.example.guardiantrack.domain.repository.ContactRepository
import com.example.guardiantrack.domain.util.toDomain
import com.example.guardiantrack.domain.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: EmergencyContactDao
) : ContactRepository {

    override fun getAllContacts(): Flow<List<EmergencyContact>> =
        contactDao.getAllContacts().map { list -> list.map { it.toDomain() } }

    override suspend fun insertContact(contact: EmergencyContact) {
        contactDao.insertContact(contact.toEntity())
    }

    override suspend fun deleteContact(id: Long) {
        contactDao.deleteContactById(id)
    }
}
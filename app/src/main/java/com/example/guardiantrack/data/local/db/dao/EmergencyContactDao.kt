package com.example.guardiantrack.data.local.db.dao

import androidx.room.*
import com.example.guardiantrack.data.local.db.entity.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContactEntity): Long

    @Delete
    suspend fun deleteContact(contact: EmergencyContactEntity)

    @Query("DELETE FROM emergency_contacts WHERE _id = :id")
    suspend fun deleteContactById(id: Long)

    @Query("SELECT * FROM emergency_contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts")
    suspend fun getAllContactsSync(): List<EmergencyContactEntity>

    @Query("SELECT * FROM emergency_contacts WHERE _id = :id LIMIT 1")
    suspend fun getContactById(id: Long): EmergencyContactEntity?
}
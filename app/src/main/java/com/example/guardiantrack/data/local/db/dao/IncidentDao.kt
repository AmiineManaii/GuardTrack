package com.example.guardiantrack.data.local.db.dao

import androidx.room.*
import com.example.guardiantrack.data.local.db.entity.IncidentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity): Long

    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incidents WHERE is_synced = 0")
    suspend fun getUnsyncedIncidents(): List<IncidentEntity>

    @Query("UPDATE incidents SET is_synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long)

    @Query("DELETE FROM incidents WHERE id = :id")
    suspend fun deleteIncidentById(id: Long)

    @Query("SELECT COUNT(*) FROM incidents")
    fun getIncidentCount(): Flow<Int>
}
package com.example.guardiantrack.domain.repository

import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {
    fun getAllIncidents(): Flow<List<Incident>>
    suspend fun insertIncident(incident: Incident): NetworkResult<Unit>
    suspend fun deleteIncident(id: Long)
    fun scheduleSyncWorker()
    suspend fun getUnsyncedIncidents(): List<com.example.guardiantrack.data.local.db.entity.IncidentEntity>
    suspend fun markAsSynced(id: Long)
}
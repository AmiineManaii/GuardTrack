package com.example.guardiantrack.data.repository

import android.content.Context
import androidx.work.*
import com.example.guardiantrack.data.local.db.dao.IncidentDao
import com.example.guardiantrack.data.local.db.entity.IncidentEntity
import com.example.guardiantrack.data.remote.api.GuardianApi
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.repository.IncidentRepository
import com.example.guardiantrack.domain.util.NetworkResult
import com.example.guardiantrack.domain.util.toDomain
import com.example.guardiantrack.domain.util.toDto
import com.example.guardiantrack.domain.util.toEntity
import com.example.guardiantrack.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentRepositoryImpl @Inject constructor(
    private val incidentDao: IncidentDao,
    private val api: GuardianApi,
    @ApplicationContext private val context: Context
) : IncidentRepository {

    override fun getAllIncidents(): Flow<List<Incident>> =
        incidentDao.getAllIncidents().map { list -> list.map { it.toDomain() } }

    override suspend fun insertIncident(incident: Incident): NetworkResult<Unit> {
        // 1. Stocker l'incident dans Room (isSynced = false par défaut via toEntity)
        val entity = incident.toEntity()
        val id = incidentDao.insertIncident(entity)

        // 2. Tenter l'envoi immédiat via Retrofit
        return try {
            val response = api.postIncident(entity.copy(id = id).toDto())
            if (response.isSuccessful) {
                // Succès : marquer comme synchronisé dans Room
                incidentDao.markAsSynced(id)
                NetworkResult.Success(Unit)
            } else {
                // Erreur HTTP : planifier WorkManager pour plus tard
                scheduleSyncWorker()
                NetworkResult.Error("HTTP ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            // Pas de réseau ou autre erreur : planifier WorkManager avec contrainte réseau
            scheduleSyncWorker()
            NetworkResult.Error(e.message ?: "Network error")
        }
    }

    /**
     * Tente de synchroniser un incident spécifique qui est déjà dans la base
     */
    suspend fun syncExistingIncident(entity: IncidentEntity): Boolean {
        return try {
            val response = api.postIncident(entity.toDto())
            if (response.isSuccessful) {
                incidentDao.markAsSynced(entity.id)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteIncident(id: Long) = incidentDao.deleteIncidentById(id)

    override fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("sync_incidents", ExistingWorkPolicy.KEEP, syncRequest)
    }

    override suspend fun getUnsyncedIncidents(): List<IncidentEntity> =
        incidentDao.getUnsyncedIncidents()

    override suspend fun markAsSynced(id: Long) = incidentDao.markAsSynced(id)
}

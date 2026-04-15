package com.example.guardiantrack.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.util.NetworkResult
import com.example.guardiantrack.domain.util.toDomain
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val incidentRepo: IncidentRepositoryImpl
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsynced = incidentRepo.getUnsyncedIncidents()
            Log.d("SyncWorker", "Syncing ${unsynced.size} incidents...")

            var allSuccess = true
            unsynced.forEach { entity ->
                val success = incidentRepo.syncExistingIncident(entity)
                if (!success) {
                    allSuccess = false
                    Log.w("SyncWorker", "Failed to sync incident ${entity.id}")
                }
            }

            if (allSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            Log.e("SyncWorker", "SyncWorker error: ${e.message}")
            Result.retry()
        }
    }
}
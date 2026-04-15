package com.example.guardiantrack.worker

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.guardiantrack.service.SurveillanceService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BootStartWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("GuardianTrack_Debug", "BootStartWorker: Exécution du worker")
        return try {
            val serviceIntent = Intent(context, SurveillanceService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
            Log.i("GuardianTrack_Debug", "BootStartWorker: Commande de démarrage envoyée au service")
            Result.success()
        } catch (e: Exception) {
            Log.e("GuardianTrack_Debug", "BootStartWorker: Échec - ${e.message}")
            Result.failure()
        }
    }

    // Requis pour setExpedited() sur Android 12+
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(context, SurveillanceService.CHANNEL_ID_SERVICE)
            .setContentTitle("GuardianTrack")
            .setContentText("Redémarrage du service...")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .build()
        
        return ForegroundInfo(SurveillanceService.NOTIFICATION_ID + 1, notification)
    }
}
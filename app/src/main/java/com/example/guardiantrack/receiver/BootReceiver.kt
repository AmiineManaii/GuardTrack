package com.example.guardiantrack.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.example.guardiantrack.worker.BootStartWorker

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("GuardianTrack_Debug", "BootReceiver: Reçu action = $action")
        
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.w("GuardianTrack_Debug", "BootReceiver: Action ignorée")
            return
        }
        
        context ?: return

        Log.i("GuardianTrack_Debug", "BootReceiver: Planification du démarrage via WorkManager (Expedited)")

        val bootWorkRequest = OneTimeWorkRequestBuilder<BootStartWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("BOOT_WORK")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "boot_start_service", 
                ExistingWorkPolicy.REPLACE, // Utiliser REPLACE pour être sûr de relancer si nécessaire
                bootWorkRequest
            )
        
        Log.d("GuardianTrack_Debug", "BootReceiver: WorkManager.enqueue appelé")
    }
}
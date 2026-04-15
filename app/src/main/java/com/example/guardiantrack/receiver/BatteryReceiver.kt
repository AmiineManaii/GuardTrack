package com.example.guardiantrack.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import com.example.guardiantrack.util.NotificationHelper
import com.example.guardiantrack.util.SmsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BatteryReceiver : BroadcastReceiver() {

    @Inject lateinit var incidentRepo: IncidentRepositoryImpl
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var smsHelper: SmsHelper
    @Inject lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != android.content.Intent.ACTION_BATTERY_LOW) return
        Log.w("BatteryReceiver", "ACTION_BATTERY_LOW received")

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Tentative de récupération de la position (lastLocation suffit ici car batterie faible)
                val location = try {
                    com.google.android.gms.tasks.Tasks.await(fusedLocationClient.lastLocation)
                } catch (e: Exception) {
                    null
                }

                val lat = location?.latitude ?: 0.0
                val lon = location?.longitude ?: 0.0

                val incident = Incident(
                    timestamp = System.currentTimeMillis(),
                    type = IncidentType.BATTERY,
                    latitude = lat,
                    longitude = lon
                )
                incidentRepo.insertIncident(incident)
                notificationHelper.showBatteryAlert()
                smsHelper.sendEmergencySms(IncidentType.BATTERY, lat, lon)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
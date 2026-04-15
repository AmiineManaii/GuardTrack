package com.example.guardiantrack.util

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import com.example.guardiantrack.data.local.datastore.UserPreferencesDataStore
import com.example.guardiantrack.data.local.security.EncryptedPreferencesManager
import com.example.guardiantrack.domain.model.IncidentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: UserPreferencesDataStore,
    private val encryptedPrefs: EncryptedPreferencesManager,
    private val notificationHelper: NotificationHelper
) {
    companion object { const val TAG = "SmsHelper" }

    fun sendEmergencySms(incidentType: IncidentType, lat: Double = 0.0, lon: Double = 0.0) {
        val isSimulation = runBlocking { dataStore.smsSimulationEnabled.first() }
        val phoneNumber = encryptedPrefs.getEmergencyNumber()

        if (phoneNumber.isBlank()) {
            Log.w(TAG, "No emergency number configured — SMS not sent")
            return
        }

        val message = buildMessage(incidentType, lat, lon)

        if (isSimulation) {
            Log.i(TAG, "SMS SIMULATION → $phoneNumber : $message")
            notificationHelper.showSimulatedSms(phoneNumber, message)
        } else {
            try {
                @Suppress("DEPRECATION")
                val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager?.sendTextMessage(phoneNumber, null, message, null, null)
                Log.i(TAG, "REAL SMS sent to $phoneNumber")
            } catch (e: Exception) {
                Log.e(TAG, "SMS send failed: ${e.message}")
            }
        }
    }

    private fun buildMessage(type: IncidentType, lat: Double, lon: Double): String {
        val locationStr = if (lat != 0.0 && lon != 0.0) {
            "\nPosition: https://www.google.com/maps?q=$lat,$lon"
        } else ""

        return when (type) {
            IncidentType.FALL -> "⚠ GuardianTrack : Chute détectée !$locationStr"
            IncidentType.BATTERY -> "🔋 GuardianTrack : Batterie critique.$locationStr"
            IncidentType.MANUAL -> "📍 GuardianTrack : Alerte manuelle déclenchée.$locationStr"
        }
    }
}
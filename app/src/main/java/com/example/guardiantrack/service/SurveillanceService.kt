package com.example.guardiantrack.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.Log
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.guardiantrack.data.local.datastore.UserPreferencesDataStore
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import com.example.guardiantrack.util.AccelerometerFilter
import com.example.guardiantrack.util.NotificationHelper
import com.example.guardiantrack.util.SmsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.sqrt

@AndroidEntryPoint
class SurveillanceService : Service(), SensorEventListener {

    companion object {
        const val CHANNEL_ID_SERVICE = "guardian_service_channel"
        const val NOTIFICATION_ID = 1001
        const val TAG = "SurveillanceService"
        private const val FREE_FALL_DURATION_MS = 100L
        private const val IMPACT_WINDOW_MS = 200L
        
        var isRunning = false
            private set
    }

    @Inject lateinit var incidentRepo: IncidentRepositoryImpl
    @Inject lateinit var dataStore: UserPreferencesDataStore
    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject lateinit var smsHelper: SmsHelper
    @Inject lateinit var notificationHelper: NotificationHelper

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var sensorHandlerThread: HandlerThread
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var freeFallStartTime: Long = 0
    private var isInFreeFall: Boolean = false
    private var freeFallThreshold = 3f
    private var impactThreshold = 15f

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION or ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }

        serviceScope.launch {
            impactThreshold = dataStore.fallThreshold.first()
            Log.d(TAG, "Impact threshold: $impactThreshold m/s²")
        }

        setupSensor()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun setupSensor() {
        sensorHandlerThread = HandlerThread("SensorThread", Process.THREAD_PRIORITY_MORE_FAVORABLE)
        sensorHandlerThread.start()

        val handler = android.os.Handler(sensorHandlerThread.looper)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME, handler)
            Log.d(TAG, "Accelerometer registered on HandlerThread")
        } ?: Log.w(TAG, "No accelerometer available on this device")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        // Application du filtre passe-bas (Bonus +3 pts)
        val result = AccelerometerFilter.processSensorData(
            event.values[0],
            event.values[1],
            event.values[2],
            event.timestamp
        )

        val magnitude = result.filteredMagnitude
        val now = System.currentTimeMillis()

        when {
            magnitude < freeFallThreshold && !isInFreeFall -> {
                isInFreeFall = true
                freeFallStartTime = now
                Log.d(TAG, "FREE FALL START — magnitude: $magnitude")
            }

            isInFreeFall && magnitude > impactThreshold -> {
                val freeFallDuration = now - freeFallStartTime
                if (freeFallDuration >= FREE_FALL_DURATION_MS && freeFallDuration <= IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS) {
                    Log.d(TAG, "FALL DETECTED — free-fall: ${freeFallDuration}ms, impact: $magnitude m/s²")
                    handleFallDetected()
                }
                isInFreeFall = false
            }

            magnitude > freeFallThreshold && isInFreeFall -> {
                val elapsed = now - freeFallStartTime
                if (elapsed > IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS) {
                    isInFreeFall = false
                    Log.d(TAG, "Free-fall timeout — no impact detected")
                }
            }
        }

        broadcastMagnitude(magnitude)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Sensor accuracy changed: $accuracy")
    }

    private fun handleFallDetected() {
        serviceScope.launch {
            val (lat, lon) = getCurrentLocation()

            val incident = Incident(
                timestamp = System.currentTimeMillis(),
                type = IncidentType.FALL,
                latitude = lat,
                longitude = lon
            )
            incidentRepo.insertIncident(incident)

            notificationHelper.showFallAlert()
            smsHelper.sendEmergencySms(IncidentType.FALL, lat, lon)

            Log.d(TAG, "Fall incident saved — lat: $lat, lon: $lon")
        }
    }

    private suspend fun getCurrentLocation(): Pair<Double, Double> {
        return try {
            // Tentative d'obtenir la position actuelle précise
            val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
            val location = com.google.android.gms.tasks.Tasks.await(
                fusedLocationClient.getCurrentLocation(priority, com.google.android.gms.tasks.CancellationTokenSource().token)
            ) ?: com.google.android.gms.tasks.Tasks.await(fusedLocationClient.lastLocation)

            Pair(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission GPS manquante: ${e.message}")
            Pair(0.0, 0.0)
        } catch (e: Exception) {
            Log.w(TAG, "GPS indisponible, tentative lastLocation: ${e.message}")
            try {
                val lastLoc = com.google.android.gms.tasks.Tasks.await(fusedLocationClient.lastLocation)
                Pair(lastLoc?.latitude ?: 0.0, lastLoc?.longitude ?: 0.0)
            } catch (e2: Exception) {
                Pair(0.0, 0.0)
            }
        }
    }

    private fun broadcastMagnitude(magnitude: Float) {
        val intent = Intent("com.example.guardiantrack.MAGNITUDE_UPDATE")
        intent.putExtra("magnitude", magnitude)
        intent.setPackage(packageName) // Restreindre le broadcast à notre propre application
        sendBroadcast(intent)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
            .setContentTitle("GuardianTrack actif")
            .setContentText("Surveillance des capteurs en cours...")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_SERVICE,
                "Service de surveillance",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Canal pour le service de surveillance GuardianTrack"
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        sensorManager.unregisterListener(this)
        sensorHandlerThread.quitSafely()
        serviceScope.cancel()
        Log.d(TAG, "SurveillanceService destroyed")
    }
}
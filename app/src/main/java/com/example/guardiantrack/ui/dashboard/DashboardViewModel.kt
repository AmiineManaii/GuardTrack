package com.example.guardiantrack.ui.dashboard

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guardiantrack.data.local.datastore.UserPreferencesDataStore
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import com.example.guardiantrack.service.SurveillanceService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class DashboardUiState(
    val isServiceRunning: Boolean = false,
    val currentMagnitude: Float = 0f,
    val batteryLevel: Int = 100,
    val isGpsActive: Boolean = false,
    val lastLocation: Location? = null,
    val alertMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incidentRepo: IncidentRepositoryImpl,
    private val dataStore: UserPreferencesDataStore,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val smsHelper: com.example.guardiantrack.util.SmsHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val magnitudeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val magnitude = intent?.getFloatExtra("magnitude", 0f) ?: 0f
            updateMagnitude(magnitude)
        }
    }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            if (level != -1 && scale != -1) {
                updateBattery((level * 100 / scale.toFloat()).toInt())
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { 
                updateGpsStatus(true, it)
                Log.d("GuardianTrack_Debug", "LocationCallback: ${it.latitude}, ${it.longitude}")
            }
        }
    }

    init {
        val magFilter = IntentFilter("com.example.guardiantrack.MAGNITUDE_UPDATE")
        ContextCompat.registerReceiver(
            context,
            magnitudeReceiver,
            magFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        
        val battFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = ContextCompat.registerReceiver(
            context,
            batteryReceiver,
            battFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        
        batteryStatus?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level != -1 && scale != -1) {
                updateBattery((level * 100 / scale.toFloat()).toInt())
            }
        }

        refreshStatus()
        monitorServiceStatus()
    }

    fun refreshStatus() {
        updateServiceStatus(isServiceRunning(SurveillanceService::class.java))
        startLocationUpdates()
    }

    private fun monitorServiceStatus() {
        viewModelScope.launch {
            while (true) {
                updateServiceStatus(isServiceRunning(SurveillanceService::class.java))
                delay(3000)
            }
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = manager.getRunningServices(Int.MAX_VALUE) ?: return false
        return runningServices.any { it.service.className == serviceClass.name }
    }

    private fun startLocationUpdates() {
        try {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build()
            
            _uiState.value = _uiState.value.copy(isGpsActive = true)

            // 1. Essayer d'obtenir la dernière position connue immédiatement
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null && _uiState.value.lastLocation == null) {
                    updateGpsStatus(true, location)
                    Log.i("GuardianTrack_Debug", "GPS: Dernière position connue obtenue: ${location.latitude}")
                }
            }

            // 2. Tenter d'obtenir la position actuelle précise
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        updateGpsStatus(true, location)
                        Log.i("GuardianTrack_Debug", "GPS: Position immédiate obtenue: ${location.latitude}")
                    }
                }

            // 3. S'abonner aux mises à jour
            fusedLocationClient.requestLocationUpdates(request, locationCallback, context.mainLooper)
        } catch (e: SecurityException) {
            updateGpsStatus(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            context.unregisterReceiver(magnitudeReceiver)
            context.unregisterReceiver(batteryReceiver)
        } catch (e: Exception) { }
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun updateMagnitude(magnitude: Float) {
        _uiState.value = _uiState.value.copy(currentMagnitude = magnitude)
    }

    fun updateBattery(level: Int) {
        _uiState.value = _uiState.value.copy(batteryLevel = level)
    }

    fun updateGpsStatus(active: Boolean, location: Location? = null) {
        _uiState.value = _uiState.value.copy(isGpsActive = active, lastLocation = location)
    }

    fun updateServiceStatus(running: Boolean) {
        _uiState.value = _uiState.value.copy(isServiceRunning = running)
    }

    @SuppressLint("MissingPermission")
    fun triggerManualAlert() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Récupérer la position la plus fraîche possible pour l'alerte
            val location = try {
                val priority = Priority.PRIORITY_HIGH_ACCURACY
                val freshLoc = fusedLocationClient.getCurrentLocation(priority, CancellationTokenSource().token).await()
                    ?: fusedLocationClient.lastLocation.await()
                freshLoc
            } catch (e: Exception) {
                Log.e("GuardianTrack", "Erreur GPS lors de l'alerte manuelle", e)
                _uiState.value.lastLocation
            }

            val lat = location?.latitude ?: 0.0
            val lon = location?.longitude ?: 0.0

            val incident = Incident(
                timestamp = System.currentTimeMillis(),
                type = IncidentType.MANUAL,
                latitude = lat,
                longitude = lon
            )
            incidentRepo.insertIncident(incident)
            
            // Envoyer SMS d'urgence pour l'alerte manuelle aussi
            smsHelper.sendEmergencySms(IncidentType.MANUAL, lat, lon)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                alertMessage = "Alerte manuelle enregistrée et envoyée"
            )
        }
    }

    fun clearAlertMessage() {
        _uiState.value = _uiState.value.copy(alertMessage = null)
    }
}

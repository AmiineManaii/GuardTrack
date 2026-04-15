package com.example.guardiantrack.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guardiantrack.service.SurveillanceService
import com.example.guardiantrack.ui.settings.SettingsViewModel
import com.example.guardiantrack.ui.theme.GuardianTrackTheme
import com.example.guardiantrack.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        permissionManager = PermissionManager(this)
        
        setContent {
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            
            // Fournir les ViewModels ici pour qu'ils soient partagés ou accessibles
            val dashboardViewModel: com.example.guardiantrack.ui.dashboard.DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            
            // Écouter quand les permissions sont accordées pour démarrer le service et rafraîchir
            permissionManager.setOnPermissionsGrantedListener {
                startSurveillanceService()
                dashboardViewModel.refreshStatus()
            }

            GuardianTrackTheme(darkTheme = uiState.darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GuardianTrackApp(dashboardViewModel = dashboardViewModel)
                }
            }
        }
        
        // Démarrer le service immédiatement si les permissions sont déjà là
        if (hasRequiredPermissions()) {
            startSurveillanceService()
        }
        
        permissionManager.requestAllPermissions()
    }

    private fun startSurveillanceService() {
        val serviceIntent = Intent(this, SurveillanceService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun hasRequiredPermissions(): Boolean {
        return PermissionManager.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::permissionManager.isInitialized) {
            permissionManager.dismissDialogs()
        }
    }
}

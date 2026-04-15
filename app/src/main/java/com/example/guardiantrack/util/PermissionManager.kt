package com.example.guardiantrack.util

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionManager(private val activity: ComponentActivity) {

    private var currentDialog: DialogInterface? = null

    companion object {
        val REQUIRED_PERMISSIONS = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.SEND_SMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    init {
        registerLauncher()
    }

    private var onPermissionsGranted: (() -> Unit)? = null

    fun setOnPermissionsGrantedListener(listener: () -> Unit) {
        onPermissionsGranted = listener
    }

    private fun registerLauncher() {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val allGranted = permissionsMap.all { it.value }
            if (allGranted) {
                onPermissionsGranted?.invoke()
            } else {
                permissionsMap.entries.forEach { (permission, granted) ->
                    if (!granted) handlePermissionDenied(permission)
                }
            }
        }
    }

    fun requestAllPermissions() {
        val missing = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isEmpty()) return

        val rationaleMsg = buildRationaleMessage(missing.toList())
        currentDialog = MaterialAlertDialogBuilder(activity)
            .setTitle("Autorisations requises")
            .setMessage(rationaleMsg)
            .setCancelable(false)
            .setPositiveButton("Continuer") { _, _ ->
                permissionLauncher.launch(missing.toTypedArray())
            }
            .setNegativeButton("Quitter") { _, _ ->
                activity.finish()
            }
            .show()
    }

    private fun handlePermissionDenied(permission: String) {
        val shouldShow = activity.shouldShowRequestPermissionRationale(permission)
        if (!shouldShow) {
            currentDialog = MaterialAlertDialogBuilder(activity)
                .setTitle("Permission bloquée")
                .setMessage("La permission a été refusée définitivement.\nActivez-la dans : Paramètres → Applications → GuardianTrack → Permissions")
                .setPositiveButton("Ouvrir les Paramètres") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }
                .setNegativeButton("Ignorer", null)
                .show()
        }
    }

    private fun buildRationaleMessage(permissions: List<String>): String {
        return permissions.joinToString("\n\n") { perm ->
            when (perm) {
                Manifest.permission.ACCESS_FINE_LOCATION ->
                    "📍 GPS : Pour géolocaliser les incidents et vous localiser en cas d'urgence."
                Manifest.permission.SEND_SMS ->
                    "📱 SMS : Pour envoyer une alerte automatique à votre contact d'urgence."
                Manifest.permission.POST_NOTIFICATIONS ->
                    "🔔 Notifications : Pour maintenir le service de surveillance actif en arrière-plan."
                else -> perm
            }
        }
    }

    /**
     * Ferme tout dialogue ouvert pour éviter les fuites de fenêtre (WindowLeaked)
     */
    fun dismissDialogs() {
        currentDialog?.dismiss()
        currentDialog = null
    }
}
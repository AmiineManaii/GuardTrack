package com.example.guardiantrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.guardiantrack.ui.theme.*

@Composable
fun PermissionRationaleDialog(
    permission  : String,
    rationale   : String,
    onConfirm   : () -> Unit,
    onDismiss   : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon    = { Icon(Icons.Outlined.Shield, null, tint = GtCyan) },
        title   = { Text("Autorisation requise", style = MaterialTheme.typography.headlineSmall, color = GtTextPrimary) },
        text    = { Text(rationale, style = MaterialTheme.typography.bodyMedium, color = GtTextSecondary) },
        containerColor = GtBgCard,
        titleContentColor = GtTextPrimary,
        textContentColor  = GtTextSecondary,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Continuer", color = GtCyan, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Plus tard", color = GtTextSecondary)
            }
        }
    )
}

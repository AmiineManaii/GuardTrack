package com.example.guardiantrack.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import com.example.guardiantrack.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentItem(
    incident      : Incident,
    onDeleteClick : () -> Unit,
    modifier      : Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state            = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val bgColor by animateColorAsState(
                targetValue   = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                                    GtRedAlert.copy(alpha = 0.3f) else Color.Transparent,
                animationSpec = tween(200),
                label         = "swipeBg"
            )
            val isDark = LocalThemeIsDark.current
            Box(
                modifier          = Modifier.fillMaxSize()
                    .background(bgColor, MaterialTheme.shapes.medium),
                contentAlignment  = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Supprimer", style = MaterialTheme.typography.labelLarge, color = if (isDark) GtTextPrimary else GtBgDeep)
                    Icon(Icons.Outlined.Delete, null, tint = if (isDark) GtTextPrimary else GtBgDeep, modifier = Modifier.size(20.dp))
                }
            }
        },
        modifier = modifier
    ) {
        GlassCard {
            val isDark = LocalThemeIsDark.current
            Row(
                modifier          = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Correction : Syntaxe de décomposition et utilisation d'icônes standards
                val (icon: ImageVector, typeColor: Color) = when (incident.type) {
                    IncidentType.FALL    -> Icons.Outlined.ErrorOutline to GtRedAlert
                    IncidentType.BATTERY -> Icons.Outlined.BatteryAlert   to GtAmber
                    IncidentType.MANUAL  -> Icons.Outlined.Warning         to (if (isDark) GtCyan else GtBgDeep)
                }

                Box(
                    modifier         = Modifier
                        .size(40.dp)
                        .background(typeColor.copy(alpha = 0.15f), MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = typeColor, modifier = Modifier.size(20.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        incident.type.name,
                        style      = MaterialTheme.typography.labelLarge,
                        color      = typeColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${incident.formattedDate}  ${incident.formattedTime}",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isDark) GtTextSecondary else Color.Gray
                    )
                    val coordsText = if (incident.latitude == 0.0 && incident.longitude == 0.0)
                        "Localisation indisponible"
                    else "%.5f, %.5f".format(incident.latitude, incident.longitude)
                    Text(coordsText, fontSize = 10.sp, color = if (isDark) GtTextDisabled else Color.Gray.copy(alpha = 0.6f))
                }

                StatusBadge(isSynced = incident.isSynced)
            }
        }
    }
}

package com.example.guardiantrack.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.guardiantrack.ui.theme.*

@Composable
fun AlertButton(
    isLoading : Boolean,
    onClick   : () -> Unit,
    modifier  : Modifier = Modifier
) {
    // Pulsation continue — scale spring
    val infiniteTransition = rememberInfiniteTransition(label = "alertPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.03f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alertScale"
    )

    Button(
        onClick  = { if (!isLoading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .scale(if (isLoading) 1f else pulseScale),
        shape  = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = GtRedAlert,
            contentColor   = GtTextPrimary,
            disabledContainerColor = GtRedDim
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color    = GtTextPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(Icons.Outlined.Warning, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                "ALERTE MANUELLE",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

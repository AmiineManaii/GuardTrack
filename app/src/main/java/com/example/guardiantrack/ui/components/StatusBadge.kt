package com.example.guardiantrack.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.ui.theme.*

@Composable
fun StatusBadge(isSynced: Boolean) {
    val bgColor by animateColorAsState(
        targetValue   = if (isSynced) GtGreen.copy(alpha = 0.2f) else GtAmber.copy(alpha = 0.2f),
        animationSpec = tween(400),
        label         = "badgeBg"
    )
    val textColor by animateColorAsState(
        targetValue   = if (isSynced) GtGreen else GtAmber,
        animationSpec = tween(400),
        label         = "badgeText"
    )

    Box(
        modifier = Modifier
            .background(bgColor, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text  = if (isSynced) "SYNC" else "EN ATTENTE",
            fontSize = 9.sp,
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

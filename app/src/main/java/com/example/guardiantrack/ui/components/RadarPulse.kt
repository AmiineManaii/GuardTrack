package com.example.guardiantrack.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.guardiantrack.ui.theme.GtCyan

/**
 * Composant Canvas — Cercles concentriques qui pulsent vers l'extérieur.
 * Utilisable en overlay sur le Dashboard pour renforcer l'esthétique radar.
 */
@Composable
fun RadarPulse(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "radar")

    // 3 ondes décalées
    val scales = (0..2).map { i ->
        transition.animateFloat(
            initialValue  = 0f,
            targetValue   = 1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(2400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(i * 800)
            ),
            label = "radarScale$i"
        )
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxR   = minOf(size.width, size.height) / 2f

        scales.forEach { scaleState ->
            val scale = scaleState.value
            val r     = maxR * scale
            val alpha = (1f - scale).coerceAtLeast(0f) * 0.5f
            drawCircle(
                color  = GtCyan.copy(alpha = alpha),
                radius = r,
                center = center,
                style  = Stroke(width = 1.5f)
            )
        }
    }
}

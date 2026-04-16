package com.example.guardiantrack.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.example.guardiantrack.ui.theme.GtCyan

import androidx.compose.ui.graphics.Color
import com.example.guardiantrack.ui.theme.GtBgCard
import com.example.guardiantrack.ui.theme.GtBorderSubtle

@Composable
fun ScanLineCard(
    isActive : Boolean,
    modifier : Modifier = Modifier,
    containerColor: Color = GtBgCard.copy(alpha = 0.4f),
    borderColor: Color = GtBorderSubtle,
    content  : @Composable () -> Unit
) {
    GlassCard(
        modifier = modifier,
        containerColor = containerColor,
        borderColor = borderColor
    ) {
        Box {
            content()

            // Scan line animée — seulement si le service est actif
            if (isActive) {
                val transition = rememberInfiniteTransition(label = "scanLine")
                val scanY by transition.animateFloat(
                    initialValue  = 0f,
                    targetValue   = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "scanY"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * scanY
                    // Ligne lumineuse principale
                    drawLine(
                        color       = GtCyan.copy(alpha = 0.4f),
                        start       = Offset(0f, y),
                        end         = Offset(size.width, y),
                        strokeWidth = 1.5f
                    )
                    // Halo sous la ligne
                    for (i in 1..8) {
                        drawLine(
                            color       = GtCyan.copy(alpha = 0.04f * (8 - i)),
                            start       = Offset(0f, y + i * 3),
                            end         = Offset(size.width, y + i * 3),
                            strokeWidth = 1f
                        )
                    }
                }
            }
        }
    }
}

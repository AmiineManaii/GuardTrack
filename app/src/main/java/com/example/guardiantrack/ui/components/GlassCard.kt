package com.example.guardiantrack.ui.components

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.guardiantrack.ui.theme.GtBgCard
import com.example.guardiantrack.ui.theme.GtBorderSubtle

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import com.example.guardiantrack.ui.theme.GtBgDeep
import com.example.guardiantrack.ui.theme.GtMagicPurple
import com.example.guardiantrack.ui.theme.LocalThemeIsDark

@Composable
fun GlassCard(
    modifier : Modifier = Modifier,
    containerColor: Color? = null,
    borderColor: Color? = null,
    content  : @Composable () -> Unit
) {
    val isDark = LocalThemeIsDark.current
    
    val finalContainerColor = containerColor ?: if (isDark) {
        GtBgCard.copy(alpha = 0.4f)
    } else {
        Color.White // Fond blanc pur en mode clair
    }
    
    val finalBorderColor = borderColor ?: if (isDark) {
        GtBorderSubtle
    } else {
        GtBgDeep.copy(alpha = 0.5f) // Bordure GtBgDeep en mode clair
    }

    Surface(
        modifier      = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, finalBorderColor, MaterialTheme.shapes.medium),
        color         = finalContainerColor,
        tonalElevation = 0.dp,
        shadowElevation = if (isDark) 0.dp else 4.dp, // Plus d'ombre en mode clair
        content       = content
    )
}

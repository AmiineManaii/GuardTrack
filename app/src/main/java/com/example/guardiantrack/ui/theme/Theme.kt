package com.example.guardiantrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GuardianDarkColorScheme = darkColorScheme(
    primary = GtMagicCyan,
    secondary = GtMagicPurple,
    tertiary = GtMagicPink,
    background = GtBgDeep,
    surface = GtBgSurface,
    surfaceVariant = GtBgCard,
    onPrimary = GtBgDeep,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = GtTextPrimary,
    onSurface = GtTextPrimary,
    onSurfaceVariant = GtTextSecondary,
    outline = GtBorderSubtle,
    error = GtRedAlert,
    onError = GtTextPrimary
)

private val GuardianLightColorScheme = lightColorScheme(
    primary = GtMagicPurple,
    secondary = GtMagicCyan,
    tertiary = GtMagicPink,
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFF1F5F9),
    surfaceVariant = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFE2E8F0),
    error = GtRedAlert,
    onError = Color.White
)

@Composable
fun GuardianTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GuardianDarkColorScheme else GuardianLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
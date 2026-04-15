package com.example.guardiantrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GuardianDarkColorScheme = darkColorScheme(
    primary = GtCyan,
    secondary = GtRedAlert,
    tertiary = GtAmber,
    background = GtBgDeep,
    surface = GtBgSurface,
    surfaceVariant = GtBgCard,
    onPrimary = GtBgDeep,
    onSecondary = GtTextPrimary,
    onTertiary = GtBgDeep,
    onBackground = GtTextPrimary,
    onSurface = GtTextPrimary,
    onSurfaceVariant = GtTextSecondary,
    outline = GtBorderSubtle,
    error = GtRedAlert,
    onError = GtTextPrimary
)

private val GuardianLightColorScheme = lightColorScheme(
    primary = GtCyan,
    secondary = GtRedAlert,
    tertiary = GtAmber,
    background = Color(0xFFF5F8FF),
    surface = Color(0xFFE8F0FA),
    surfaceVariant = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0D1520),
    onSurface = Color(0xFF0D1520),
    onSurfaceVariant = Color(0xFF5D6B82),
    outline = Color(0xFFD1E0F5),
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
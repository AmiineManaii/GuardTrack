package com.example.guardiantrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalThemeIsDark = staticCompositionLocalOf { false }

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
    background = Color.White,
    surface = Color.White,
    surfaceVariant = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = GtBgDeep,
    onSurface = GtBgDeep,
    onSurfaceVariant = GtTextSecondary,
    outline = GtBgDeep,
    error = GtRedAlert,
    onError = Color.White
)

@Composable
fun GuardianTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GuardianDarkColorScheme else GuardianLightColorScheme

    CompositionLocalProvider(LocalThemeIsDark provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
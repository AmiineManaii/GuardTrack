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

@Composable
fun GlassCard(
    modifier : Modifier = Modifier,
    content  : @Composable () -> Unit
) {
    Surface(
        modifier      = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        color         = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content       = content
    )
}

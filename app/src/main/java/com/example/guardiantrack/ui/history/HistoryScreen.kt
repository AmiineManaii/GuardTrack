package com.example.guardiantrack.ui.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import com.example.guardiantrack.ui.components.IncidentItem
import com.example.guardiantrack.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onExportCsv: () -> Unit,
    onExportTxt: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showExportMenu by remember { mutableStateOf(false) }

    val isDark = LocalThemeIsDark.current
    val magicGradient = if (isDark) {
        Brush.verticalGradient(colors = listOf(GtBgDeep, GtBgSurface))
    } else {
        Brush.verticalGradient(colors = listOf(Color.White, Color.White))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(magicGradient)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "JOURNAL",
                fontSize = 28.sp,
                color = if (isDark) GtMagicCyan else GtBgDeep,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            
            AnimatedVisibility(
                visible = uiState.incidents.isNotEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Box {
                    Button(
                        onClick = { showExportMenu = true },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) GtMagicCyan else GtBgDeep),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Exporter", color = if (isDark) GtBgDeep else Color.White, fontWeight = FontWeight.Bold)
                    }

                    DropdownMenu(
                        expanded = showExportMenu,
                        onDismissRequest = { showExportMenu = false },
                        modifier = Modifier.background(if (isDark) GtBgCard else Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("CSV", color = if (isDark) GtTextPrimary else GtBgDeep) },
                            onClick = {
                                showExportMenu = false
                                onExportCsv()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("TEXTE", color = if (isDark) GtTextPrimary else GtBgDeep) },
                            onClick = {
                                showExportMenu = false
                                onExportTxt()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = uiState.incidents.isEmpty() to uiState.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
            },
            label = "contentState"
        ) { (isEmpty, isLoading) ->
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = if (isDark) GtMagicCyan else GtBgDeep)
                    }
                }
                isEmpty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📋",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Aucun incident enregistré",
                                fontSize = 14.sp,
                                color = if (isDark) GtTextSecondary else GtBgDeep.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = uiState.incidents,
                            key = { it.id }
                        ) { incident ->
                            IncidentItem(
                                incident = incident,
                                onDeleteClick = { viewModel.deleteIncident(incident.id) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}

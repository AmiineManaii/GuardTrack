package com.example.guardiantrack.ui.dashboard

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.ui.components.*
import com.example.guardiantrack.ui.theme.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAlertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Observer pour rafraîchir les données (GPS/Service) dès que l'écran devient actif
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshStatus() 
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val magnitudeAnimated by animateFloatAsState(
        targetValue = uiState.currentMagnitude,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "magnitude"
    )
    
    val magicGradient = Brush.verticalGradient(
        colors = listOf(GtBgDeep, GtBgSurface)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(magicGradient)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GUARDIAN",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = GtMagicCyan,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                AnimatedContent(
                    targetState = uiState.isServiceRunning,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "serviceStatus"
                ) { isRunning ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(if (isRunning) GtGreen else GtRedAlert, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isRunning) "SURVEILLANCE ACTIVE" else "SERVICE INACTIF",
                            fontSize = 11.sp,
                            color = if (isRunning) GtGreen else GtRedAlert,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (uiState.isServiceRunning) {
                Box(contentAlignment = Alignment.Center) {
                    RadarPulse(modifier = Modifier.size(48.dp))
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .scale(pulseScale)
                            .background(GtGreen, RoundedCornerShape(7.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main Card: Accelerometer
        ScanLineCard(
            isActive = uiState.isServiceRunning,
            modifier = Modifier.fillMaxWidth().height(220.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "ACCÉLÉROMÈTRE",
                    fontSize = 11.sp,
                    color = GtMagicPurple,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format("%.2f", magnitudeAnimated),
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = GtMagicCyan,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "m/s²",
                        fontSize = 18.sp,
                        color = GtTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LinearProgressIndicator(
                    progress = { (magnitudeAnimated / 30f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = when {
                        magnitudeAnimated > 20f -> GtRedAlert
                        magnitudeAnimated > 15f -> GtAmber
                        else -> GtMagicCyan
                    },
                    trackColor = GtBgDeep.copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grid: Battery and GPS
        Row(modifier = Modifier.fillMaxWidth()) {
            GlassCard(
                modifier = Modifier.weight(1f).height(140.dp),
                containerColor = GtBgCard.copy(alpha = 0.6f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "BATTERIE", fontSize = 10.sp, color = GtTextSecondary, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val batteryColor = when {
                        uiState.batteryLevel <= 15 -> GtRedAlert
                        uiState.batteryLevel <= 30 -> GtAmber
                        else -> GtGreen
                    }
                    Text(text = "${uiState.batteryLevel}%", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = batteryColor, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.weight(1f))
                    LinearProgressIndicator(
                        progress = { uiState.batteryLevel / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = batteryColor,
                        trackColor = GtBgDeep.copy(alpha = 0.5f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            GlassCard(
                modifier = Modifier.weight(1f).height(140.dp),
                containerColor = GtBgCard.copy(alpha = 0.6f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(if (uiState.isGpsActive) GtGreen else GtTextSecondary, RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (uiState.isGpsActive) "GPS ACTIF" else "GPS INACTIF", fontSize = 10.sp, color = if (uiState.isGpsActive) GtGreen else GtTextSecondary, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.lastLocation?.let { loc ->
                        Column {
                            Row {
                                Text(text = "LAT: ", fontSize = 11.sp, color = GtTextSecondary, fontFamily = FontFamily.Monospace)
                                Text(text = "%.5f".format(loc.latitude), fontSize = 12.sp, color = GtMagicCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Text(text = "LON: ", fontSize = 11.sp, color = GtTextSecondary, fontFamily = FontFamily.Monospace)
                                Text(text = "%.5f".format(loc.longitude), fontSize = 12.sp, color = GtMagicCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    } ?: Text(text = "RECHERCHE...", fontSize = 12.sp, color = GtMagicCyan.copy(alpha = 0.5f), fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        AlertButton(isLoading = uiState.isLoading, onClick = onAlertClick)

        uiState.alertMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Snackbar(
                modifier = Modifier.animateContentSize(),
                action = { TextButton(onClick = { viewModel.clearAlertMessage() }) { Text("OK", color = GtMagicCyan) } },
                containerColor = GtBgCardElevated,
                contentColor = GtTextPrimary,
                shape = RoundedCornerShape(16.dp)
            ) { Text(message) }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}
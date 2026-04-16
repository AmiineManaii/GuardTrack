package com.example.guardiantrack.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.ui.components.GlassCard
import com.example.guardiantrack.ui.theme.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.guardiantrack.ui.components.CircularSlider

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var emergencyNumber by remember { mutableStateOf(uiState.emergencyNumber) }
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var showSavedMessage by remember { mutableStateOf(false) }

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
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "CONFIGURATION",
            fontSize = 28.sp,
            color = if (isDark) GtMagicCyan else GtBgDeep,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Circular Slider Section (Sensitivity)
        MagicSection(title = "SENSIBILITÉ") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seuil de détection d'impact",
                    fontSize = 14.sp,
                    color = if (isDark) GtTextSecondary else GtBgDeep.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                CircularSlider(
                    value = uiState.threshold,
                    onValueChange = { viewModel.setThreshold(it) },
                    valueRange = 5f..30f,
                    size = 180.dp,
                    activeColor = if (isDark) GtMagicCyan else GtBgDeep
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (uiState.threshold < 12f) "Sensibilité Élevée" 
                           else if (uiState.threshold < 20f) "Sensibilité Normale" 
                           else "Sensibilité Basse",
                    color = if (uiState.threshold < 12f) GtRedAlert else if (uiState.threshold < 20f) (if (isDark) GtGreen else GtBgDeep) else GtAmber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Communication Section
        MagicSection(title = "COMMUNICATIONS") {
            SettingsSwitchRow(
                title = "Simulation SMS",
                subtitle = "Mode développeur actif",
                checked = uiState.smsSimulation,
                onCheckedChange = { viewModel.setSmsSimulation(it) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = if (isDark) GtDivider else Color.LightGray.copy(alpha = 0.5f))
            
            Column {
                Text(
                    text = "Numéro d'urgence principal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) GtTextPrimary else GtBgDeep,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = emergencyNumber,
                        onValueChange = { emergencyNumber = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("+216 XX XXX XXX", color = if (isDark) GtTextSecondary.copy(alpha = 0.5f) else Color.Gray)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isDark) GtMagicCyan else GtBgDeep,
                            unfocusedBorderColor = if (isDark) GtBorderSubtle else Color.LightGray,
                            focusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                            unfocusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                            cursorColor = if (isDark) GtMagicCyan else GtBgDeep
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.setEmergencyNumber(emergencyNumber)
                            showSavedMessage = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) GtMagicCyan else GtBgDeep),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("SAVE", color = if (isDark) GtBgDeep else Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                AnimatedVisibility(
                    visible = showSavedMessage,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = "✓ Configuré avec succès",
                        fontSize = 12.sp,
                        color = if (isDark) GtGreen else GtBgDeep,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Appearance
        MagicSection(title = "STYLE") {
            SettingsSwitchRow(
                title = "Mode Sombre Magique",
                subtitle = "Utiliser les couleurs cosmiques",
                checked = uiState.darkMode,
                onCheckedChange = { viewModel.setDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Secondary Contacts
        MagicSection(title = "CONTACTS SECONDAIRES") {
            Column {
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text("Nom complet") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GtMagicPurple else GtBgDeep,
                        unfocusedBorderColor = if (isDark) GtBorderSubtle else Color.LightGray,
                        focusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                        unfocusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                        focusedLabelColor = if (isDark) GtMagicPurple else GtBgDeep,
                        unfocusedLabelColor = if (isDark) GtTextSecondary else Color.Gray
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it.filter { c -> c.isDigit() } },
                        label = { Text("Téléphone") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isDark) GtMagicPurple else GtBgDeep,
                            unfocusedBorderColor = if (isDark) GtBorderSubtle else Color.LightGray,
                            focusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                            unfocusedTextColor = if (isDark) GtTextPrimary else GtBgDeep,
                            focusedLabelColor = if (isDark) GtMagicPurple else GtBgDeep,
                            unfocusedLabelColor = if (isDark) GtTextSecondary else Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    IconButton(
                        onClick = {
                            if (contactName.isNotBlank() && contactPhone.isNotBlank()) {
                                viewModel.addContact(contactName, contactPhone)
                                contactName = ""
                                contactPhone = ""
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(if (isDark) GtMagicPurple else GtBgDeep, RoundedCornerShape(16.dp))
                    ) {
                        Text("+", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (uiState.contacts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    uiState.contacts.forEach { contact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(contact.name, color = if (isDark) GtTextPrimary else GtBgDeep, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text(contact.phoneNumber, color = if (isDark) GtTextSecondary else Color.Gray, fontSize = 13.sp)
                            }
                            IconButton(onClick = { viewModel.deleteContact(contact.id) }) {
                                Text("✕", color = GtRedAlert.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun MagicSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GtMagicPurple,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = GtBgCard.copy(alpha = 0.6f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = GtTextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = GtTextSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GtMagicCyan,
                checkedTrackColor = GtMagicCyan.copy(alpha = 0.3f),
                uncheckedThumbColor = GtTextDisabled,
                uncheckedTrackColor = GtBgDeep
            )
        )
    }
}
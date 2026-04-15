package com.example.guardiantrack.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.background
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "PARAMÈTRES",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsCard(title = "Seuil de détection", subtitle = "Sensibilité de l'accéléromètre") {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "5",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                    Slider(
                        value = uiState.threshold,
                        onValueChange = { viewModel.setThreshold(it) },
                        valueRange = 5f..30f,
                        steps = 24,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = GtCyan,
                            activeTrackColor = GtCyan,
                            inactiveTrackColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        text = "30",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = "Valeur actuelle : ${ "%.1f".format(uiState.threshold) } m/s²",
                    fontSize = 12.sp,
                    color = GtCyan,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Apparence") {
            SettingsSwitchRow(
                title = "Mode sombre",
                subtitle = "Interface en thème sombre",
                checked = uiState.darkMode,
                onCheckedChange = { viewModel.setDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Notifications") {
            SettingsSwitchRow(
                title = "Mode Simulation SMS",
                subtitle = "Afficher les SMS au lieu de les envoyer",
                checked = uiState.smsSimulation,
                onCheckedChange = { viewModel.setSmsSimulation(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Urgence", subtitle = "Numéro principal pour les alertes") {
            Column {
                Text(
                    text = "Numéro d'urgence",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = emergencyNumber,
                        onValueChange = { emergencyNumber = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("+216 XX XXX XXX", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GtCyan,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.setEmergencyNumber(emergencyNumber)
                            showSavedMessage = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GtCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("OK", color = GtBgDeep)
                    }
                }

                AnimatedVisibility(
                    visible = showSavedMessage,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = "✓ Numéro sauvegardé",
                        fontSize = 12.sp,
                        color = GtGreen,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Contacts d'urgence", subtitle = "Liste secondaire") {
            Column {
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text("Nom du contact") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GtCyan,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it.filter { c -> c.isDigit() } },
                        label = { Text("Téléphone") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GtCyan,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Button(
                        onClick = {
                            if (contactName.isNotBlank() && contactPhone.isNotBlank()) {
                                viewModel.addContact(contactName, contactPhone)
                                contactName = ""
                                contactPhone = ""
                            }
                        },
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GtCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("AJOUTER", color = GtBgDeep)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                uiState.contacts.forEach { contact ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(contact.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                            Text(contact.phoneNumber, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                        IconButton(onClick = { viewModel.deleteContact(contact.id) }) {
                            Text("✕", color = GtRedAlert)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = GtCyan
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
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
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GtCyan,
                checkedTrackColor = GtCyan.copy(alpha = 0.3f)
            )
        )
    }
}
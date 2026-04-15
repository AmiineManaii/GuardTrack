package com.example.guardiantrack.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guardiantrack.data.local.datastore.UserPreferencesDataStore
import com.example.guardiantrack.data.local.security.EncryptedPreferencesManager
import com.example.guardiantrack.domain.model.EmergencyContact
import com.example.guardiantrack.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val threshold: Float = 15.0f,
    val darkMode: Boolean = false,
    val smsSimulation: Boolean = true,
    val emergencyNumber: String = "",
    val contacts: List<EmergencyContact> = emptyList()
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val encryptedPrefs: EncryptedPreferencesManager,
    private val contactRepo: ContactRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        dataStore.fallThreshold,
        dataStore.darkMode,
        dataStore.smsSimulationEnabled,
        contactRepo.getAllContacts()
    ) { threshold, dark, sms, contacts ->
        SettingsUiState(
            threshold = threshold,
            darkMode = dark,
            smsSimulation = sms,
            emergencyNumber = encryptedPrefs.getEmergencyNumber(),
            contacts = contacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun addContact(name: String, number: String) {
        viewModelScope.launch {
            contactRepo.insertContact(EmergencyContact(name = name, phoneNumber = number))
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            contactRepo.deleteContact(id)
        }
    }

    fun setThreshold(value: Float) {
        viewModelScope.launch { dataStore.setFallThreshold(value) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { dataStore.setDarkMode(enabled) }
    }

    fun setSmsSimulation(enabled: Boolean) {
        viewModelScope.launch { dataStore.setSmsSimulation(enabled) }
    }

    fun setEmergencyNumber(number: String) {
        encryptedPrefs.saveEmergencyNumber(number)
    }
}

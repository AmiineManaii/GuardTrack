package com.example.guardiantrack.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "guardian_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_THRESHOLD = floatPreferencesKey("fall_threshold")
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        val KEY_EMERGENCY_NUM = stringPreferencesKey("emergency_number_encrypted_ref")
        val KEY_SMS_SIMULATION = booleanPreferencesKey("sms_simulation")
        const val DEFAULT_THRESHOLD = 15.0f
    }

    val fallThreshold: Flow<Float> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_THRESHOLD] ?: DEFAULT_THRESHOLD }

    val darkMode: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_DARK_MODE] ?: false }

    val smsSimulationEnabled: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_SMS_SIMULATION] ?: true }

    suspend fun setFallThreshold(value: Float) {
        context.dataStore.edit { it[KEY_THRESHOLD] = value }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }

    suspend fun setSmsSimulation(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SMS_SIMULATION] = enabled }
    }
}
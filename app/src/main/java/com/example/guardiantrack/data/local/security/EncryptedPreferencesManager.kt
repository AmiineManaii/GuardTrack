package com.example.guardiantrack.data.local.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "guardian_secure_prefs"
        private const val KEY_EMERGENCY = "encrypted_emergency_number"
        private const val KEY_API_KEY = "encrypted_api_key"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveEmergencyNumber(number: String) {
        sharedPreferences.edit().putString(KEY_EMERGENCY, number).apply()
    }

    fun getEmergencyNumber(): String =
        sharedPreferences.getString(KEY_EMERGENCY, "") ?: ""

    fun saveApiKey(key: String) {
        sharedPreferences.edit().putString(KEY_API_KEY, key).apply()
    }

    fun getApiKey(): String =
        sharedPreferences.getString(KEY_API_KEY, "") ?: ""
}
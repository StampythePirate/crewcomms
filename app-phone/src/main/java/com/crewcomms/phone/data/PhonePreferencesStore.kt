package com.crewcomms.phone.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "phone_settings")

@Singleton
class PhonePreferencesStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val settings: Flow<PhoneSettings> = context.dataStore.data.map { prefs ->
        PhoneSettings(
            displayName = prefs[DISPLAY_NAME] ?: "Captain",
            vibrationRelayToWatch = prefs[VIBRATION_RELAY] ?: true,
            keepScreenAwake = prefs[KEEP_SCREEN_AWAKE] ?: false,
            autoReconnect = prefs[AUTO_RECONNECT] ?: true,
            useMockTransport = prefs[USE_MOCK] ?: true,
        )
    }

    suspend fun updateDisplayName(name: String) {
        context.dataStore.edit { it[DISPLAY_NAME] = name }
    }

    suspend fun updateVibrationRelay(enabled: Boolean) {
        context.dataStore.edit { it[VIBRATION_RELAY] = enabled }
    }

    suspend fun updateKeepScreenAwake(enabled: Boolean) {
        context.dataStore.edit { it[KEEP_SCREEN_AWAKE] = enabled }
    }

    suspend fun updateAutoReconnect(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_RECONNECT] = enabled }
    }

    suspend fun updateUseMock(enabled: Boolean) {
        context.dataStore.edit { it[USE_MOCK] = enabled }
    }

    companion object {
        private val DISPLAY_NAME = stringPreferencesKey("display_name")
        private val VIBRATION_RELAY = booleanPreferencesKey("vibration_relay")
        private val KEEP_SCREEN_AWAKE = booleanPreferencesKey("keep_screen_awake")
        private val AUTO_RECONNECT = booleanPreferencesKey("auto_reconnect")
        private val USE_MOCK = booleanPreferencesKey("use_mock")
    }
}

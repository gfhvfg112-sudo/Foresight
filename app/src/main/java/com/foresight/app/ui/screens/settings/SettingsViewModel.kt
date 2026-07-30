package com.foresight.app.ui.screens.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val darkMode: String = "system",
    val defaultAlertDays: Int = 7,
    val isPremium: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    companion object {
        val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val KEY_DARK_MODE = stringPreferencesKey("dark_mode")
        val KEY_DEFAULT_ALERT_DAYS = stringPreferencesKey("default_alert_days")
        val KEY_IS_PREMIUM = booleanPreferencesKey("is_premium")
    }

    init {
        viewModelScope.launch {
            context.dataStore.data.collect { preferences ->
                _uiState.update {
                    it.copy(
                        notificationsEnabled = preferences[KEY_NOTIFICATIONS] ?: true,
                        darkMode = preferences[KEY_DARK_MODE] ?: "system",
                        defaultAlertDays = (preferences[KEY_DEFAULT_ALERT_DAYS] ?: "7").toIntOrNull() ?: 7,
                        isPremium = preferences[KEY_IS_PREMIUM] ?: false
                    )
                }
            }
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { it[KEY_NOTIFICATIONS] = enabled }
        }
    }

    fun updateDarkMode(mode: String) {
        viewModelScope.launch {
            context.dataStore.edit { it[KEY_DARK_MODE] = mode }
        }
    }

    fun updateDefaultAlertDays(days: Int) {
        viewModelScope.launch {
            context.dataStore.edit { it[KEY_DEFAULT_ALERT_DAYS] = days.toString() }
        }
    }
}

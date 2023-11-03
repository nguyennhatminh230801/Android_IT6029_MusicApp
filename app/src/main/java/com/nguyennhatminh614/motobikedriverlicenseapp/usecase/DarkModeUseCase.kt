package com.nguyennhatminh614.motobikedriverlicenseapp.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class DarkModeUseCase(
    private val settingDataStore: DataStore<Preferences>,
) {
    val currentDarkModeState = settingDataStore.data
        .map { preference -> preference[IS_DARK_MODE] }

    suspend fun invokeChangeDarkModeState() {
        settingDataStore.edit { preference ->
            preference[IS_DARK_MODE] = preference[IS_DARK_MODE]?.not() ?: false
        }
    }

    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }
}
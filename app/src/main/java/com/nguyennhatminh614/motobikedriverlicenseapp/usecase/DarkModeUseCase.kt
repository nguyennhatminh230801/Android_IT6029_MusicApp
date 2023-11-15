package com.nguyennhatminh614.motobikedriverlicenseapp.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nguyennhatminh614.motobikedriverlicenseapp.data.datastore.KeyDataStore
import kotlinx.coroutines.flow.map

class DarkModeUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    val currentDarkModeState = dataStore.data
        .map { preference -> preference[KeyDataStore.isDarkMode] }

    suspend fun invokeChangeDarkModeState() {
        dataStore.edit { preference ->
            preference[KeyDataStore.isDarkMode] = preference[KeyDataStore.isDarkMode]?.not() ?: false
        }
    }
}
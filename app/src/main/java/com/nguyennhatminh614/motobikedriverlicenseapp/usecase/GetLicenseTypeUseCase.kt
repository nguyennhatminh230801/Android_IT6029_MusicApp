package com.nguyennhatminh614.motobikedriverlicenseapp.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nguyennhatminh614.motobikedriverlicenseapp.data.datastore.KeyDataStore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import kotlinx.coroutines.flow.map

class GetLicenseTypeUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    val currentLicenseTypeState = dataStore.data
        .map { preference ->
            val currentLicenseType = preference[KeyDataStore.currentLicenseType]
            LicenseType.values().firstOrNull { it.type == currentLicenseType } ?: LicenseType.A1
        }

    suspend fun invokeChangeLicenseTypeState(value: String) {
        dataStore.edit { preference ->
            preference[KeyDataStore.currentLicenseType] = value
        }
    }

    suspend fun invokeChangeLicenseTypeState(value: LicenseType) {
        dataStore.edit { preference ->
            preference[KeyDataStore.currentLicenseType] = value.type
        }
    }
}
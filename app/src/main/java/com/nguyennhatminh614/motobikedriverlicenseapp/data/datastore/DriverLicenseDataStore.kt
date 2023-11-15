package com.nguyennhatminh614.motobikedriverlicenseapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.driverLicenseDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "driver_license")

object KeyDataStore {
    val isDarkMode = booleanPreferencesKey("is_dark_mode")
    val currentLicenseType = stringPreferencesKey("current_license_type")
}

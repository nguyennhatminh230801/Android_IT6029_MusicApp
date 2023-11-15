package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.data.datastore.driverLicenseDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataStoreModule = module {
    single { androidApplication().driverLicenseDataStore }
}
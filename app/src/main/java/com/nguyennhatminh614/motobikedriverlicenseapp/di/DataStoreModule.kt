package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.data.datastore.settingDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {
    single(named("setting_data_store")) { androidContext().settingDataStore }
}
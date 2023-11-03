package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    single { DarkModeUseCase(get(named("setting_data_store"))) }
}
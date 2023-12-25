package com.nguyennhatminh614.motobikedriverlicenseapp.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.nguyennhatminh614.motobikedriverlicenseapp.di.apiModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.dataStoreModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.databaseModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.examDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.networkModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.questionDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.repositoryModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.studyDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.tipsHighScoreDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.trafficSignDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.useCaseModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.viewModelModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.wrongAnswerDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MotorbikeApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val TAG = "MotorbikeApplication"

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MotorbikeApplication)

            modules(
                databaseModule,
                apiModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                tipsHighScoreDataSourceModule,
                examDataSourceModule,
                studyDataSourceModule,
                wrongAnswerDataSourceModule,
                trafficSignDataSourceModule,
                questionDataSourceModule,
                dataStoreModule,
                useCaseModule,
            )
        }

        applicationScope.launch {
            val isDarkMode =  inject<DarkModeUseCase>().value.currentDarkModeState.first() ?: false

            withContext(Dispatchers.Main) {
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) MODE_NIGHT_YES
                    else MODE_NIGHT_NO
                )
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
    }
}

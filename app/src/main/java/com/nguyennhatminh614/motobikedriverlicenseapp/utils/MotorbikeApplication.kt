package com.nguyennhatminh614.motobikedriverlicenseapp.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.di.apiModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.databaseModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.examDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.networkModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.repositoryModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.sharedPreferenceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.studyDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.tipsHighScoreDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.trafficSignDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.viewModelModule
import com.nguyennhatminh614.motobikedriverlicenseapp.di.wrongAnswerDataSourceModule
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.isCurrentDarkMode
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.setCurrentLicenseType
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MotorbikeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MotorbikeApplication)

            modules(
                sharedPreferenceModule,
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
            )
        }

        val sharedPreferences = inject<SharedPreferences>().value

        if (sharedPreferences.getString(AppConstant.CURRENT_LICENSE_TYPE, AppConstant.EMPTY_DATA)?.isEmpty() == true) {
            sharedPreferences.setCurrentLicenseType(LicenseType.A1.type)
        }

        if (sharedPreferences.isCurrentDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}

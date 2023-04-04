package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant

class ChangeLicenseTypeViewModel(
    private val sharedPreferences: SharedPreferences,
) : BaseViewModel() {

    private val _currentLicenseType = MutableLiveData<LicenseType>()

    val currentLicenseType: LiveData<LicenseType>
        get() = _currentLicenseType

    init {
        if (sharedPreferences.getString(AppConstant.CURRENT_LICENSE_TYPE, AppConstant.EMPTY_DATA)?.isEmpty() == true) {
            sharedPreferences.edit().putString(AppConstant.CURRENT_LICENSE_TYPE, LicenseType.A1.type).apply()
        }
        getCurrentLicenseType()
    }

    fun onChangingToNewLicenseType(currentLicenseType: LicenseType) {
        sharedPreferences.edit().putString(AppConstant.CURRENT_LICENSE_TYPE, currentLicenseType.type).apply()
        getCurrentLicenseType()
    }

    private fun getCurrentLicenseType() {
        val currentLicenseType =
            sharedPreferences.getString(AppConstant.CURRENT_LICENSE_TYPE, LicenseType.A1.type)
                ?: LicenseType.A1.type

        val licenseType = enumValues<LicenseType>().first{ it.type == currentLicenseType}
        _currentLicenseType.postValue(licenseType)
    }
}
package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.getAllMotorbikeLicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentLicenseType

class InstructionViewModel(
    private val sharedPreferences: SharedPreferences
): BaseViewModel(){

    private val _isMotorbikeLicenseType = MutableLiveData<Boolean>()
    val isMotorbikeLicenseType: LiveData<Boolean>
        get() = _isMotorbikeLicenseType

    init {
        checkCurrentLicenseType()
    }

    private fun checkCurrentLicenseType() {
        launchTask {
            val isMotorbikeLicense = sharedPreferences.getCurrentLicenseType() in getAllMotorbikeLicenseType()
            _isMotorbikeLicenseType.value = isMotorbikeLicense
            hideLoading()
        }
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.SharedPreferences
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.getAllMotorbikeLicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentLicenseType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InstructionViewModel(
    private val sharedPreferences: SharedPreferences
): BaseViewModel(){

    private val _isMotorbikeLicenseType : MutableStateFlow<Boolean?> = MutableStateFlow(null)

    val isMotorbikeLicenseType: StateFlow<Boolean?> = _isMotorbikeLicenseType.asStateFlow()

    init {
        checkCurrentLicenseType()
    }

    private fun checkCurrentLicenseType() {
        launchTask {
            _isMotorbikeLicenseType.update {
                sharedPreferences.getCurrentLicenseType() in getAllMotorbikeLicenseType()
            }
            hideLoading()
        }
    }
}

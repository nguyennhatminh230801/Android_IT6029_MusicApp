package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    getLicenseTypeUseCase: GetLicenseTypeUseCase,
) : BaseViewModel(){

    val currentLicenseTypeState = getLicenseTypeUseCase
        .currentLicenseTypeState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}
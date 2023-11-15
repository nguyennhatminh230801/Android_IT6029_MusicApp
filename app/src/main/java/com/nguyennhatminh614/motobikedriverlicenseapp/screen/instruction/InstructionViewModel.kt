package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class InstructionViewModel(
    getLicenseTypeUseCase: GetLicenseTypeUseCase,
    darkModeUseCase: DarkModeUseCase,
): BaseViewModel(){

    val currentLicenseType = getLicenseTypeUseCase.currentLicenseTypeState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val currentDarkModeState = darkModeUseCase.currentDarkModeState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}

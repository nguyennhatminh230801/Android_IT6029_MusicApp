package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChangeLicenseTypeViewModel(
    private val getLicenseTypeUseCase: GetLicenseTypeUseCase,
) : BaseViewModel() {

    val currentLicenseType = getLicenseTypeUseCase.currentLicenseTypeState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun onChangingToNewLicenseType(currentLicenseType: LicenseType) {
        viewModelScope.launch {
            getLicenseTypeUseCase.invokeChangeLicenseTypeState(currentLicenseType)
        }
    }
}
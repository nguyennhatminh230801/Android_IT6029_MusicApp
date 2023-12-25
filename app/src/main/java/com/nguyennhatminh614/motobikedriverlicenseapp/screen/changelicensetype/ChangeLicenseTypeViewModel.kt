package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main.MainScreenViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeLicenseTypeViewModel(
    private val getLicenseTypeUseCase: GetLicenseTypeUseCase,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        initLicenseTypeState()
    }

    private fun initLicenseTypeState() {
        getLicenseTypeUseCase.currentLicenseTypeState
            .map { currentLicenseType ->
                LicenseType.values().map { licenseType ->
                    LicenseTypeItemUiState(
                        licenseType = licenseType,
                        isSelected = licenseType == currentLicenseType,
                    )
                }
            }
            .onEach { listLicenseTypeUiState ->
                _uiState.update {
                    it.copy(
                        licenseTypes = listLicenseTypeUiState
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )
    }

    fun onChangingToNewLicenseType(currentLicenseType: LicenseType) {
        viewModelScope.launch {
            getLicenseTypeUseCase.invokeChangeLicenseTypeState(currentLicenseType)
        }
    }

    data class UiState(
        val licenseTypes: List<LicenseTypeItemUiState> = emptyList()
    )

    data class LicenseTypeItemUiState(
        val licenseType: LicenseType,
        val isSelected: Boolean = false,
    )
}
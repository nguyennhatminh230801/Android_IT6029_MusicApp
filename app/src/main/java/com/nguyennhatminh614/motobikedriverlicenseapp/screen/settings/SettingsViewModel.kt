package com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val darkModeUseCase: DarkModeUseCase,
) : BaseViewModel() {

    val currentDarkModeState: StateFlow<Boolean?> =
        darkModeUseCase.currentDarkModeState
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null,
            )

    fun invokeChangeDarkModeState() {
        viewModelScope.launch {
            darkModeUseCase.invokeChangeDarkModeState()
        }
    }
}

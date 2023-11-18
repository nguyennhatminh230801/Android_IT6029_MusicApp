package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel(
    private val getLicenseTypeUseCase: GetLicenseTypeUseCase,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch { getCategories() }
            launch { getCurrentLicenseType() }
        }
    }

    private suspend fun getCurrentLicenseType() {
        val currentLicenseType = getLicenseTypeUseCase.currentLicenseTypeState.first()
        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(
                    currentLicenseType = currentLicenseType,
                )
            }
        }
    }

    private fun getCategories() {
        _uiState.update {
            it.copy(
                categories = MainCategoryFactory.createCategories()
            )
        }
    }

    fun getLicenseType(): LicenseType? {
        return _uiState.value.currentLicenseType
    }

    data class UiState(
        val categories: List<CategoryItemUiState> = emptyList(),
        val currentLicenseType: LicenseType? = null,
    )

    data class CategoryItemUiState(
        @DrawableRes
        val thumbnailDrawId: Int,
        @StringRes
        val titleStrId: Int,
        val type: CategoryType,
    )
}
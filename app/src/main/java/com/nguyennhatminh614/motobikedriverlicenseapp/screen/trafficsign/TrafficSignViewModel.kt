package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSignCategory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSignsEntity
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.TrafficRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processDoubleQuotes
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processEndline
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrafficSignViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val trafficRepository: TrafficRepository,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        launchTask {
            trafficRepository.getAllTrafficSignal()
                .onRight { trafficSigns ->
                    val result = trafficSigns
                        .groupBy { it.category }
                        .toList()
                        .mapIndexed { index, (category, trafficSigns) ->
                            val newKey = when(category) {
                                TrafficSignCategory.RESTRICT_TRAFFIC_SIGNAL -> R.string.restrict_traffic_signal
                                TrafficSignCategory.COMMAND_TRAFFIC_SIGNAL -> R.string.command_traffic_signal
                                TrafficSignCategory.INSTRUCTION_TRAFFIC_SIGNAL -> R.string.instruction_traffic_signal
                                TrafficSignCategory.SUB_TRAFFIC_SIGNAL -> R.string.sub_traffic_signal
                                TrafficSignCategory.WARNING_TRAFFIC_SIGNAL -> R.string.warning_traffic_signal
                            }

                            TabTrafficSignUiState(
                                id = index + 1,
                                title = newKey,
                                trafficSigns = trafficSigns
                            )
                        }

                    _uiState.update {
                        it.copy(
                            trafficSignTab = result,
                        )
                    }

                    hideLoading()
                }
                .onLeft { hideLoading() }
        }
    }

    fun updateTrafficSignTab(tab: TabTrafficSignUiState) {
        val newData = _uiState.value.trafficSignTab.map { tabUiState ->
            if (tabUiState.id == tab.id) {
                tabUiState.copy(scrolledPosition = tab.scrolledPosition)
            } else tabUiState.copy()
        }.toList()

        _uiState.update {
            it.copy(trafficSignTab = newData)
        }
    }

    data class UiState(
        val trafficSignTab: List<TabTrafficSignUiState> = emptyList(),
    )

    data class TabTrafficSignUiState(
        val id: Int,
        @StringRes
        val title: Int,
        val trafficSigns: List<TrafficSigns>,
        val scrolledPosition: Int = 0,
    )
}

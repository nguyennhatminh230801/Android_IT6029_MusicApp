package com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores

import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.TipsHighScoreRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TipsHighScoreViewModel(
    private val repository: TipsHighScoreRepository,
) : BaseViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        launchTask {
            repository.getTipsHighScore()
                .onRight { tipsHighScore ->
                    _uiState.update {
                        it.copy(
                            tipHighScoreUiStates = tipsHighScore.map { data ->
                                TipHighScoreUiState(data.id, data.title, data.content)
                            }
                        )
                    }

                    hideLoading()
                }
                .onLeft { hideLoading() }
        }
    }

    fun notifySpanStateForItem(item: TipHighScoreUiState) {
        _uiState.update {
            it.copy(
                tipHighScoreUiStates = _uiState.value.tipHighScoreUiStates.map {tips ->
                    if (item.id == tips.id) {
                        tips.copy(isExpanded = tips.isExpanded.not())
                    } else tips
                }
            )
        }
    }

    data class UiState(
        val tipHighScoreUiStates: List<TipHighScoreUiState> = emptyList(),
    )

    data class TipHighScoreUiState(
        val id: Int,
        val title: String,
        val content: String,
        val isExpanded: Boolean = false,
    )
}

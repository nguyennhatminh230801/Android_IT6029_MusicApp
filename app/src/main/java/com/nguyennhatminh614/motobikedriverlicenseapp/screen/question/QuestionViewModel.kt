package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question

import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.Question
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionAnswers
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.usecase.GetAllWrongAnswerQuestionUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class QuestionViewModel(
    val getAllWrongAnswerQuestionUseCase: GetAllWrongAnswerQuestionUseCase,
) : BaseViewModel() {

    private val _currentQuestionScreenType: MutableStateFlow<QuestionScreenType?> = MutableStateFlow(null)

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        _currentQuestionScreenType
            .filterNotNull()
            .onEach {
                if (it == QuestionScreenType.WrongAnswer) {
                    _uiState.update { uiState ->
                        uiState.copy(
                            questionsItemsUiStates = provideWrongAnswerQuestions()
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun provideWrongAnswerQuestions() : List<QuestionItemUiState> {
        return getAllWrongAnswerQuestionUseCase()?.mapIndexed { index, question ->
            QuestionItemUiState(
                id = index,
                question = question,
                answersUiState = question.answers.mapIndexed { idx , answer ->
                    QuestionAnswerUiState(
                        id = idx,
                        questionId = question.id,
                        questionAnswers = answer,
                    )
                },
                isCurrentQuestion = index == _uiState.value.currentPosition
            )
        } ?: emptyList()
    }

    fun updateCurrentQuestionScreenType(questionScreenType: QuestionScreenType) {
        _currentQuestionScreenType.update { questionScreenType }
    }

    fun nextPage() {
        if (!isLastPage()) {
            val newPosition = _uiState.value.currentPosition + 1
            _uiState.update { uiState ->
                uiState.copy(
                    questionsItemsUiStates = uiState.questionsItemsUiStates.mapIndexed { idx, item ->
                        item.copy(
                            isCurrentQuestion = idx == newPosition,
                        )
                    },
                    currentPosition = newPosition,
                )
            }
        }
    }

    fun previousPage() {
        if (!isFirstPage()) {
            val newPosition = _uiState.value.currentPosition - 1
            _uiState.update { uiState ->
                uiState.copy(
                    questionsItemsUiStates = uiState.questionsItemsUiStates.mapIndexed { idx, item ->
                        item.copy(
                            isCurrentQuestion = idx == newPosition,
                        )
                    },
                    currentPosition = newPosition,
                )
            }
        }
    }

    fun updateCurrentPage(position: Int) {
        _uiState.update {
            it.copy(
                questionsItemsUiStates = it.questionsItemsUiStates.mapIndexed { idx, item ->
                    item.copy(
                        isCurrentQuestion = idx == position,
                    )
                },
                currentPosition = position,
            )
        }
    }

    fun updateSelectedAnswerState(data: QuestionAnswerUiState) {
        _uiState.update {
            it.copy(
                questionsItemsUiStates = it.questionsItemsUiStates.map { questionItem ->
                    if (questionItem.answersUiState.any { it == data }) {
                        questionItem.copy(
                            answersUiState = questionItem.answersUiState.map { answer ->
                                if (answer == data) {
                                    answer.copy(
                                        state = if (questionItem.question.correctAnswer == answer.questionAnswers) {
                                            StateQuestionOption.CORRECT
                                        } else {
                                            StateQuestionOption.INCORRECT
                                        }
                                    )
                                } else answer.copy(state = StateQuestionOption.UNSELECTED)
                            }
                        )
                    } else questionItem
                }
            )
        }
    }

    fun isFirstPage(): Boolean {
        return _uiState.value.isFirstPage
    }

    fun isLastPage(): Boolean {
        return _uiState.value.isLastPage
    }

    fun updateCurrentUiState(uiState: UiState) {
        _uiState.update { uiState }
    }

    fun moveToCurrentQuestion(questionItemUiState: QuestionItemUiState) {
        val currentPosition = _uiState.value.questionsItemsUiStates.indexOf(questionItemUiState).takeIf { it >= 0 }

        currentPosition?.let {
            _uiState.update {
                it.copy(
                    questionsItemsUiStates = it.questionsItemsUiStates.mapIndexed { idx, item ->
                        item.copy(isCurrentQuestion = idx == currentPosition)
                    },
                    currentPosition = currentPosition,
                )
            }
        }
    }

    data class UiState(
        val questionsItemsUiStates: List<QuestionItemUiState> = emptyList(),
        val currentPosition: Int = 0,
    ) {
        val isFirstPage: Boolean
            get() = currentPosition == 0
        val isLastPage: Boolean
            get() = currentPosition == questionsItemsUiStates.lastIndex
    }

    data class QuestionItemUiState(
        val id: Int,
        val question: Question,
        val answersUiState: List<QuestionAnswerUiState>,
        val isCurrentQuestion: Boolean = false,
    ) {
        val isAnySelected : Boolean
            get() = answersUiState.any { it.state != StateQuestionOption.UNSELECTED }

        val selectedItem : QuestionAnswerUiState?
            get() = answersUiState.firstOrNull { it.state != StateQuestionOption.UNSELECTED }
    }

    data class QuestionAnswerUiState(
        val id: Int,
        val questionId: Int,
        val questionAnswers: QuestionAnswers,
        val state: StateQuestionOption = StateQuestionOption.UNSELECTED,
    )
}
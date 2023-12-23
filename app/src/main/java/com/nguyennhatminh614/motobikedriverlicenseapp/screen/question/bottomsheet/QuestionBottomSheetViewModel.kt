package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.bottomsheet

import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class QuestionBottomSheetViewModel : BaseViewModel() {

    private val _uiState: MutableStateFlow<QuestionViewModel.UiState> = MutableStateFlow(QuestionViewModel.UiState())


}
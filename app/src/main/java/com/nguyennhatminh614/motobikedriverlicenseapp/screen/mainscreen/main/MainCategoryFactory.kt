package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main

import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.R

object MainCategoryFactory {
    fun createCategories(): List<MainScreenViewModel.CategoryItemUiState> {
        return listOf(
            MainScreenViewModel.CategoryItemUiState(R.drawable.ic_exam, R.string.exam_category, CategoryType.EXAM),
            MainScreenViewModel.CategoryItemUiState(R.drawable.ic_study, R.string.study_category, CategoryType.STUDY),
            MainScreenViewModel.CategoryItemUiState(R.drawable.ic_signal, R.string.signal_category, CategoryType.SIGNAL),
            MainScreenViewModel.CategoryItemUiState(R.drawable.ic_tips_high_score, R.string.tips_high_score_category, CategoryType.TIPS_HIGH_SCORE),
            MainScreenViewModel.CategoryItemUiState(R.drawable.ic_wrong_answer, R.string.wrong_answer_category, CategoryType.WRONG_ANSWER),
            MainScreenViewModel.CategoryItemUiState(
                R.drawable.ic_driving_business,
                R.string.change_license_type_category,
                CategoryType.EXAM_GUIDE
            ),
            MainScreenViewModel.CategoryItemUiState(
                R.drawable.ic_change_license_type_main_screen,
                R.string.exam_guide_category,
                CategoryType.CHANGE_LICENSE_TYPE
            ),
            MainScreenViewModel.CategoryItemUiState(
                R.drawable.ic_settings,
                R.string.settings_category,
                CategoryType.SETTINGS
            )
        )
    }

    fun createCategoriesDiffUtil(): DiffUtil.ItemCallback<MainScreenViewModel.CategoryItemUiState> {
        return object : DiffUtil.ItemCallback<MainScreenViewModel.CategoryItemUiState>() {
            override fun areItemsTheSame(
                oldItem: MainScreenViewModel.CategoryItemUiState,
                newItem: MainScreenViewModel.CategoryItemUiState,
            ): Boolean = oldItem.type == newItem.type

            override fun areContentsTheSame(
                oldItem: MainScreenViewModel.CategoryItemUiState,
                newItem: MainScreenViewModel.CategoryItemUiState,
            ): Boolean = oldItem == newItem
        }
    }
}
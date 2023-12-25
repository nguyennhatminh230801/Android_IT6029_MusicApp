package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype.ChangeLicenseTypeViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction.InstructionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main.MainScreenViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.bottomsheet.QuestionBottomSheetViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings.SettingsViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.study.StudyViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores.TipsHighScoreViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign.TrafficSignViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BaseViewModel)
    viewModelOf(::TipsHighScoreViewModel)
    viewModelOf(::ExamViewModel)
    viewModelOf(::StudyViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::TrafficSignViewModel)
    viewModelOf(::InstructionViewModel)
    viewModelOf(::ChangeLicenseTypeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::QuestionViewModel)
    viewModelOf(::QuestionBottomSheetViewModel)
}

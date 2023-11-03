package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype.ChangeLicenseTypeViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction.InstructionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings.SettingsViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.study.StudyViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores.TipsHighScoreViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign.TrafficSignViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer.WrongAnswerViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { TipsHighScoreViewModel(get()) }
    viewModel { ExamViewModel(get(), get(), get()) }
    viewModel { StudyViewModel(get(),get()) }
    viewModel { WrongAnswerViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { TrafficSignViewModel(get()) }
    viewModel { InstructionViewModel(get()) }
    viewModel { ChangeLicenseTypeViewModel(get()) }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.usecase.GetAllWrongAnswerQuestionUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.GetLicenseTypeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::DarkModeUseCase)
    factoryOf(::GetLicenseTypeUseCase)

    //Wrong Answer
    factoryOf(::GetAllWrongAnswerQuestionUseCase)
}
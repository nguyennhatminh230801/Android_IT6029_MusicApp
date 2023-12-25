package com.nguyennhatminh614.motobikedriverlicenseapp.di

import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.ExamRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IExamDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IQuestionDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IStudyDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.ITipsHighScoreDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.ITrafficSignalDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IWrongAnswerDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.QuestionRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.StudyRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.TipsHighScoreRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.TrafficRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.WrongAnswerRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.exam.LocalExamDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.study.StudyLocalDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.wronganswer.WrongAnswerLocalDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.exam.RemoteExamDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.question.RemoteQuestionDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.study.StudyRemoteDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.tipshighscore.RemoteTipsHighScoreDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.trafficsign.TrafficSignRemoteDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TipsHighScoreRepository)
    singleOf(::ExamRepository)
    singleOf(::StudyRepository)
    singleOf(::WrongAnswerRepository)
    singleOf(::TrafficRepository)
    singleOf(::QuestionRepository)
}

val tipsHighScoreDataSourceModule = module {
    singleOf(::RemoteTipsHighScoreDataSource) { bind<ITipsHighScoreDataSource.Remote>() }
}

val examDataSourceModule = module {
    singleOf(::LocalExamDataSource) { bind<IExamDataSource.Local>() }
    singleOf(::RemoteExamDataSource) { bind<IExamDataSource.Remote>() }
}

val studyDataSourceModule = module {
    singleOf(::StudyLocalDataSource) { bind<IStudyDataSource.Local>() }
    singleOf(::StudyRemoteDataSource) { bind<IStudyDataSource.Remote>() }
}

val wrongAnswerDataSourceModule = module {
    singleOf(::WrongAnswerLocalDataSource) { bind<IWrongAnswerDataSource.Local>() }
}

val trafficSignDataSourceModule = module {
    singleOf(::TrafficSignRemoteDataSource) { bind<ITrafficSignalDataSource.Remote>() }
}

val questionDataSourceModule = module {
    singleOf(::RemoteQuestionDataSource) { bind<IQuestionDataSource.Remote>() }
}
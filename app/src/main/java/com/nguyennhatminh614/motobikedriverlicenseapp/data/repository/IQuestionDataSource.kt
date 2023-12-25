package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import arrow.core.Either
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.Question
import kotlinx.coroutines.flow.Flow

interface IQuestionDataSource {
    interface Remote {
        suspend fun getQuestions(): Either<Exception, List<Question>>
        fun getQuestionsAsFlow(): Flow<Either<Exception, List<Question>>>
    }
}
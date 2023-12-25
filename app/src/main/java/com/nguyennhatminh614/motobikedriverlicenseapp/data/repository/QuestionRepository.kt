package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import arrow.core.Either
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.Question
import kotlinx.coroutines.flow.Flow

class QuestionRepository(
    val remote: IQuestionDataSource.Remote,
) : IQuestionDataSource.Remote {
    override suspend fun getQuestions(): Either<Exception, List<Question>> = remote.getQuestions()
    override fun getQuestionsAsFlow(): Flow<Either<Exception, List<Question>>> = remote.getQuestionsAsFlow()
}
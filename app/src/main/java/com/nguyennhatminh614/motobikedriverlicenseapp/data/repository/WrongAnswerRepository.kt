package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer

class WrongAnswerRepository(
    private val local: IWrongAnswerDataSource.Local,
) : IWrongAnswerDataSource.Local {

    override suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer> =
        local.getAllWrongAnswerQuestion()

    override suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.insertNewWrongAnswerQuestion(wrongAnswer)

    override suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.updateWrongAnswerQuestion(wrongAnswer)

    override suspend fun findWrongAnswerQuestionByID(id: Int)
        = local.findWrongAnswerQuestionByID(id)
}

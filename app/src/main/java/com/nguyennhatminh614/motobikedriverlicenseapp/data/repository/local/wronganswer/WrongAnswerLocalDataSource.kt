package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.wronganswer

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IWrongAnswerDataSource

class WrongAnswerLocalDataSource(
    private val wrongAnswerDao: WrongAnswerDao,
) : IWrongAnswerDataSource.Local {

    override suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer> =
        wrongAnswerDao.getAllWrongAnswerQuestion()

    override suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        wrongAnswerDao.insertNewWrongAnswerQuestion(wrongAnswer)

    override suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        wrongAnswerDao.updateWrongAnswerQuestion(wrongAnswer)

    override suspend fun checkWrongAnswerQuestionExists(id: Int): Boolean =
        wrongAnswerDao.checkWrongAnswerQuestionExists(id) > NOT_FOUNDED_RECORD

    companion object {
        private const val NOT_FOUNDED_RECORD = 0
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class WrongAnswerRepository(
    private val local: IWrongAnswerDataSource.Local,
    private val remote: IWrongAnswerDataSource.Remote,
) : IWrongAnswerDataSource.Local, IWrongAnswerDataSource.Remote {

    override suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer> =
        local.getAllWrongAnswerQuestion()

    override suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.insertNewWrongAnswerQuestion(wrongAnswer)

    override suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.updateWrongAnswerQuestion(wrongAnswer)

    override suspend fun checkWrongAnswerQuestionExists(id: Int): Boolean =
        local.checkWrongAnswerQuestionExists(id)

    override suspend fun getAllListQuestion(listener: IResponseListener<MutableList<NewQuestion>>) {
        remote.getAllListQuestion(listener)
    }

}

package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

interface IWrongAnswerDataSource {
    interface Local {
        suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer>
        suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer)
        suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer)
        suspend fun findWrongAnswerQuestionByID(id: Int): WrongAnswer?
    }

    interface Remote {
        suspend fun getAllListQuestion(listener: IResponseListener<MutableList<NewQuestion>>)
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.utils

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant

fun processQuestionOptionsList(questionsID: Int, listString: List<String>) =
    mutableListOf<QuestionOptions>().apply {
        var index = AppConstant.FIRST_INDEX
        listString.forEach {
            this.add(QuestionOptions(questionsID, index, it))
            index++
        }
    }

fun generateEmptyQuestionStateList(listQuestion: List<NewQuestion>): MutableList<QuestionOptions> {
    return mutableListOf<QuestionOptions>().apply {
        listQuestion.forEach {
            add(provideEmptyQuestionOption(questionID = it.id))
        }
    }
}

fun provideEmptyQuestionOption(questionID: Int)
    = QuestionOptions(questionID, AppConstant.NONE_POSITION, "")
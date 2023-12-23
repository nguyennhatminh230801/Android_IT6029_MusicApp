package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions

import com.nguyennhatminh614.motobikedriverlicenseapp.utils.IDataConverter

object QuestionDataConverter : IDataConverter<QuestionItemResponse, Question> {
    override fun convert(source: QuestionItemResponse): Question {
        val questionAnswers = createQuestionAnswers(source.listOption, source.id)

        return Question(
            id = source.id,
            questionTitle = source.question,
            answers = questionAnswers,
            bannerUrl = source.image,
            correctAnswer = questionAnswers.first { it.id == source.correctAnswerPosition },
            type = QuestionType.values().first { it.type == source.questionType },
            explain = source.explain,
            isFailedImmediatelyQuestion = source.isImmediateFailedTestWhenWrong
        )
    }

    private fun createQuestionAnswers(options: List<String>, questionId: Int): List<QuestionAnswers> {
        return options.mapIndexed { index, item ->
            QuestionAnswers(
                id = index,
                questionId = questionId,
                title = item,
            )
        }
    }
}
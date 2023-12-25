package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.usecase

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.Question
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.QuestionRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.WrongAnswerRepository

class GetAllWrongAnswerQuestionUseCase(
    private val wrongAnswerRepository: WrongAnswerRepository,
    private val questionRepository: QuestionRepository,
) {
    suspend operator fun invoke() : List<Question>? {
        val wrongAnswer = wrongAnswerRepository.getAllWrongAnswerQuestion().map { it.questionsID }

        questionRepository.getQuestions()
            .onRight { list -> return list.filter { it.id in wrongAnswer } }

        return null
    }
}
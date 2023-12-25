package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions

data class Question(
    val id: Int,
    val questionTitle: String,
    val answers: List<QuestionAnswers>,
    val correctAnswer: QuestionAnswers,
    val bannerUrl: String,
    val type: QuestionType,
    val explain: String,
    val isFailedImmediatelyQuestion: Boolean
)

data class QuestionAnswers(
    val id: Int,
    val questionId: Int,
    val title: String,
)
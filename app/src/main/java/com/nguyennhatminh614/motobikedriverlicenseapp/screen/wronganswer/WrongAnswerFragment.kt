package com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer

import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionScreenType

class WrongAnswerFragment : QuestionFragment() {
    override fun provideScreenType(): QuestionScreenType {
        return QuestionScreenType.WrongAnswer
    }
}

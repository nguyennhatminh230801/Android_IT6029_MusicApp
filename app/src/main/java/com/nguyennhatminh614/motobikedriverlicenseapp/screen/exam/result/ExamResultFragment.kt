package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.result

import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentExamResultBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getSelectedColor
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExamResultFragment
    : BaseFragment<FragmentExamResultBinding>(FragmentExamResultBinding::inflate) {

    override val viewModel by sharedViewModel<ExamViewModel>()

    private val questionExamAdapter by lazy { ExamResultQuestionAdapter() }

    override fun initData() {
        viewBinding.apply {
            recyclerViewExamQuestion.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = questionExamAdapter
            }
        }
        viewModel.getCurrentExam()?.let { updateData(it) }
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        //Not implement
    }

    private fun updateData(exam: Exam) = with(viewBinding) {
        textExamStateResult.apply {
            when (exam.examState) {
                ExamState.PASSED.value -> {
                    text = "Đạt"
                    setBackgroundResource(R.drawable.bg_corner_20dp_green_pastel)
                }

                ExamState.FAILED.value -> {
                    text = "Không đạt"
                    setBackgroundResource(R.drawable.bg_corner_20dp_red_pastel)
                }
            }
        }

        textExamResultDescription.text = textExamStateResult.text.toString()

        textTimeDone.text = "10:00"
        textExamDone.text = "${exam.numbersOfCorrectAnswer}/${exam.listQuestionOptions.size}"

        textCorrectAnswer.text =
            exam.listQuestionOptions.count { selection -> selection.stateNumber == StateQuestionOption.CORRECT.type }
                .toString()
        textWrongAnswer.text =
            exam.listQuestionOptions.count { selection -> selection.stateNumber == StateQuestionOption.INCORRECT.type }
                .toString()
        textNotAnswered.text =
            exam.listQuestionOptions.count { selection -> selection.stateNumber == StateQuestionOption.UNKNOWN.type }
                .toString()

        questionExamAdapter.submitList(exam.listQuestions)
    }
}
package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.result

import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestionWithState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentExamResultBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentThemeBackgroundColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getSelectedColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.isCurrentDarkMode
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.toDateTimeMMSS
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ExamResultFragment
    : BaseFragment<FragmentExamResultBinding>(FragmentExamResultBinding::inflate) {

    override val viewModel by sharedViewModel<ExamViewModel>()

    private val questionExamAdapter by lazy { ExamResultQuestionAdapter() }

    private val smoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    override fun initData() {

        viewBinding.apply {
            val isDarkModeOn = inject<SharedPreferences>().value.isCurrentDarkMode()

            if (isDarkModeOn) {
                layoutExamResult.setBackgroundColor(getCurrentThemeBackgroundColor())
            } else {
                layoutExamResult.setBackgroundColor(getSelectedColor(R.color.background_color))
            }

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

        textTimeDone.text = exam.timeExamDone.toDateTimeMMSS()

        textExamDone.text = "${exam.numbersOfCorrectAnswer}/${exam.listQuestionOptions.size}"

        textCorrectAnswer.text =
            exam.listQuestionOptions.count { selection -> selection.stateNumber == StateQuestionOption.CORRECT.type }
                .toString()
        textWrongAnswer.text =
            exam.listQuestionOptions.count { selection ->
                selection.stateNumber == StateQuestionOption.INCORRECT.type
                        && selection.position != AppConstant.NONE_POSITION
            }
                .toString()
        //Với các câu không trả lời được thì vị trí sẽ là -1
        textNotAnswered.text =
            exam.listQuestionOptions.count { selection -> selection.position == AppConstant.NONE_POSITION }
                .toString()

        questionExamAdapter.submitList(exam.listQuestions.map { NewQuestionWithState(it) })
        questionExamAdapter.updateQuestionStateList(exam.listQuestionOptions)

        questionExamAdapter.setUpdateCallBack {
            smoothScroller.targetPosition = it
            viewBinding.recyclerViewExamQuestion.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }
}
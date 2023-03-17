package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Questions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentQuestionLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter.QuestionOptionAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processExplainQuestion
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionExamFragment
    : BaseFragment<FragmentQuestionLayoutBinding>(FragmentQuestionLayoutBinding::inflate) {

    private var questionsPosition = AppConstant.NONE_POSITION
    private var questions: Questions? = null
    private var lastSelectedPosition = AppConstant.NONE_POSITION

    override val viewModel by sharedViewModel<ExamViewModel>()

    private val questionOptionAdapter by lazy { QuestionOptionAdapter() }

    override fun initData() {
        questions?.let { question ->
            viewBinding.apply {
                textTitleQuestions.text = question.question
                textQuestionExplain.text = question.explain.processExplainQuestion()

                //Chỉ sử dụng Cheat mode cho mục đích kiểm thử
                textCheatModeAnswer.text = question.answer

                viewQuestionExplain.visibility = View.INVISIBLE

                if (question.hasImageBanner) {
                    imageQuestions.loadGlideImageFromUrl(
                        context,
                        question.imageUrl
                    )
                } else {
                    imageQuestions.isVisible = false
                    (recyclerViewQuestionOptions.layoutManager as? ConstraintLayout.LayoutParams)?.topToBottom =
                        textTitleQuestions.id
                }

                viewBinding.recyclerViewQuestionOptions.adapter = questionOptionAdapter
                val initListQuestionOptions = processQuestionOptionsList(question.option)
                questionOptionAdapter.submitList(initListQuestionOptions)

                if (viewModel.getCurrentExam()?.examState != ExamState.UNDEFINED.value) {
                    questionOptionAdapter.disableClickEvent()
                }
            }
        }
    }

    override fun handleEvent() {
        if (viewModel.getCurrentExam()?.examState == ExamState.UNDEFINED.value) {
            questionOptionAdapter.registerOnClickItemEvent { item ->
                questionOptionAdapter.updateStateListWithPosition(updateStateListUI(item),
                    item.position)

                viewModel.updateStateQuestion(questionsPosition, item)
                viewModel.navigateToNextQuestion(questionsPosition)
            }
        }
    }

    private fun processQuestionOptionsList(listString: List<String>) =
        mutableListOf<QuestionOptions>().apply {
            var index = AppConstant.FIRST_INDEX
            listString.forEach {
                this.add(QuestionOptions(index, it))
                index++
            }
        }

    private fun updateStateListUI(item: QuestionOptions?, isNotRunningTest: Boolean = false): MutableList<QuestionOptions> {
        val newList = questionOptionAdapter.currentList.toMutableList()
        if (item != null && item.position != AppConstant.NONE_POSITION) {
            val pos = item.position

            if (isNotRunningTest){
                if (lastSelectedPosition != AppConstant.NONE_POSITION) {
                    newList[lastSelectedPosition].stateNumber = StateQuestionOption.UNKNOWN.type
                }

                if (item.data == questions?.answer) {
                    newList[pos].stateNumber = StateQuestionOption.CORRECT.type
                    viewBinding.viewQuestionExplain.visibility = View.VISIBLE
                } else {
                    newList[pos].stateNumber = StateQuestionOption.INCORRECT.type
                    viewBinding.viewQuestionExplain.visibility = View.INVISIBLE
                }

            }

            lastSelectedPosition = pos
        }

        return newList
    }

    override fun bindData() {
        viewModel.listExam.observe(viewLifecycleOwner) {
            val data = viewModel.getCurrentExam()?.listQuestionOptions
            data?.let {
                if (questionsPosition != AppConstant.NONE_POSITION) {
                    val currentState = data[questionsPosition]
                    val isNotRunningTest = viewModel.getCurrentExam()?.examState != ExamState.UNDEFINED.value
                    questionOptionAdapter.updateStateListWithPosition(updateStateListUI(currentState, isNotRunningTest),
                        currentState.position)
                }
            }
        }
    }

    companion object {
        fun newInstance(
            questionPosition: Int,
            questions: Questions,
        ) =
            QuestionExamFragment().apply {
                this.questionsPosition = questionPosition
                this.questions = questions
            }
    }
}

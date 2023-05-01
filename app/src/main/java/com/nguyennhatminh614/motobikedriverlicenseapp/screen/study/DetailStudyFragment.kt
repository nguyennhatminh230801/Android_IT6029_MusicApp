package com.nguyennhatminh614.motobikedriverlicenseapp.screen.study

import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentDetailStudyLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.QuestionDetailAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.BottomSheetQuestionDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IBottomSheetListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailStudyFragment :
    BaseFragment<FragmentDetailStudyLayoutBinding>(FragmentDetailStudyLayoutBinding::inflate) {

    private var listQuestionSize = AppConstant.EMPTY_SIZE
    override val viewModel by sharedViewModel<StudyViewModel>()

    private val bottomSheetDialog by lazy {
        BottomSheetQuestionDialog()
    }

    private val questionAdapter by lazy { QuestionDetailAdapter() }

    private val bottomSheetDialogCallBack by lazy {
        object : IBottomSheetListener {
            override fun onNextQuestion(listener: IResponseListener<Int>) {
                if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                    viewBinding.viewPagerQuestions.currentItem++
                    listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                } else {
                    listener.onError(null)
                }
            }

            override fun onPreviousQuestion(listener: IResponseListener<Int>) {
                if (viewBinding.viewPagerQuestions.currentItem > AppConstant.FIRST_INDEX) {
                    viewBinding.viewPagerQuestions.currentItem--
                    listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                } else {
                    listener.onError(null)
                }
            }

            override fun onClickMoveToPosition(index: Int, data: QuestionOptions) {
                viewBinding.viewPagerQuestions.currentItem = index
            }
        }
    }

    override fun initData() {
        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewBinding.textCurrentQuestions.text =
                        "Câu ${position + 1}/${listQuestionSize}"
                }
            }
        )

        viewBinding.viewPagerQuestions.adapter = questionAdapter
    }

    override fun handleEvent() {
        viewBinding.buttonNextQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                viewBinding.viewPagerQuestions.currentItem++
            }
        }

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem > AppConstant.FIRST_INDEX) {
                viewBinding.viewPagerQuestions.currentItem--
            }
        }

        viewBinding.bottomBar.setOnClickListener {
            context?.let { notNullContext ->
                bottomSheetDialog.initDialog(
                    notNullContext,
                    viewModel.listStudyCategory.value?.get(
                        viewModel.currentStudyCategory.value
                            ?: AppConstant.NONE_POSITION
                    )
                        ?.listQuestionsState,
                    viewBinding.viewPagerQuestions.currentItem
                )
                bottomSheetDialog.setDialogEvent(bottomSheetDialogCallBack)
                bottomSheetDialog.showDialog()
            }
        }

        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateDataQuestionPos(questionID, questionPos, questionOptions)
            viewModel.saveProgressToDatabase()

            if (questionOptions.stateNumber == StateQuestionOption.INCORRECT.type) {
                viewModel.saveWrongAnswerObjectToDatabase(
                    WrongAnswer(
                        questionOptions.questionsID,
                        System.currentTimeMillis()
                    )
                )
            }
        }
    }

    override fun bindData() {
        viewModel.listStudyCategory.observe(viewLifecycleOwner) {
            val currentPosition =
                viewModel.currentStudyCategory.value ?: AppConstant.NONE_POSITION
            listQuestionSize = it[currentPosition].totalNumberOfQuestions
            questionAdapter.submitList(it[currentPosition].listQuestions)
            questionAdapter.updateQuestionStateList(it[currentPosition].listQuestionsState)

            viewBinding.textCurrentQuestions.text =
                "Câu ${viewBinding.viewPagerQuestions.currentItem + 1}/${listQuestionSize}"
            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible =
                it[currentPosition].listQuestions.isEmpty()
            viewBinding.layoutVisibleWhenHaveData.isVisible =
                it[currentPosition].listQuestions.isEmpty().not()
        }
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam

import android.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentDetailExamLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.QuestionDetailAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.ViewPagerAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant.EMPTY_SIZE
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant.FIRST_INDEX
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.BottomSheetQuestionDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.generateEmptyQuestionStateList
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IBottomSheetListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailExamFragment :
    BaseFragment<FragmentDetailExamLayoutBinding>(FragmentDetailExamLayoutBinding::inflate) {

    private var listQuestionSize = EMPTY_SIZE

    private val bottomSheetDialog by lazy {
        BottomSheetQuestionDialog()
    }

    private val questionAdapter by lazy {
        QuestionDetailAdapter()
    }

    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.getCurrentExam()?.examState == ExamState.UNDEFINED.value) {
                    AlertDialog.Builder(context)
                        .setTitle(DIALOG_TITLE)
                        .setMessage(DIALOG_MESSAGE)
                        .setPositiveButton(YES_BUTTON) { _, _ ->
                            viewModel.saveCurrentExamState()
                            findNavController().navigateUp()
                        }
                        .setNegativeButton(NO_BUTTON) { _, _ -> }
                        .create()
                        .show()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override val viewModel by sharedViewModel<ExamViewModel>()

    override fun initData() {
        viewBinding.viewPagerQuestions.adapter = questionAdapter

        if (viewModel.getCurrentExamTimestampLeft() != NO_TIME_LEFT) {
            viewModel.startCountDownEvent(
                onFinishExamEvent = {
                    findNavController().navigateUp()
                }
            )
            questionAdapter.disableShowExplanation()
            questionAdapter.disableShowCorrectAnswer()
        } else {
            viewBinding.textCurrentTime.isVisible = false
            (viewBinding.textCurrentQuestions.layoutParams as?
                    ConstraintLayout.LayoutParams)?.horizontalBias = CENTER_TEXT_QUESTION
            viewBinding.root.requestLayout()
            questionAdapter.disableClickEvent()
        }
    }

    override fun handleEvent() {
        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateStateQuestion(questionPos, questionOptions)
            viewModel.navigateToNextQuestion(viewBinding.viewPagerQuestions.currentItem)
        }
        viewBinding.buttonNextQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                viewBinding.viewPagerQuestions.currentItem++
            }
        }

        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewBinding.textCurrentQuestions.text =
                        "Câu ${viewBinding.viewPagerQuestions.currentItem + 1}/${listQuestionSize}"
                }
            }
        )

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem > FIRST_INDEX) {
                viewBinding.viewPagerQuestions.currentItem--
            }
        }

        viewBinding.bottomBar.setOnClickListener {
            context?.let { notNullContext ->
                bottomSheetDialog.initDialog(
                    notNullContext,
                    viewModel.getCurrentExam()?.listQuestionOptions,
                    viewBinding.viewPagerQuestions.currentItem,
                )

                bottomSheetDialog.setDialogEvent(
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
                            if (viewBinding.viewPagerQuestions.currentItem > FIRST_INDEX) {
                                viewBinding.viewPagerQuestions.currentItem--
                                listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                            } else {
                                listener.onError(null)
                            }
                        }

                        override fun onClickMoveToPosition(index: Int, data: QuestionOptions) {
                            viewBinding.viewPagerQuestions.currentItem = index
                        }
                    })

                bottomSheetDialog.examDialogMode(isExam = true,
                    isRunning = viewModel.getCurrentExamTimestampLeft() != NO_TIME_LEFT)

                bottomSheetDialog.showDialog()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun bindData() {
        viewModel.currentTimeCountDown.observe(viewLifecycleOwner) {
            viewBinding.textCurrentTime.text = it
        }

        viewModel.currentExamQuestionPosition.observe(viewLifecycleOwner) {
            if (it != AppConstant.NONE_POSITION) {
                lifecycleScope.launch {
                    delay(CHANGE_TO_NEXT_QUESTION_DELAY_TIME)
                    viewBinding.viewPagerQuestions.currentItem = it
                }
            }
        }

        viewModel.listExam.observe(viewLifecycleOwner) {
            val currentExamPosition =
                viewModel.currentExamPosition.value ?: AppConstant.NONE_POSITION
            if (currentExamPosition != AppConstant.NONE_POSITION) {
                val currentExam = it[currentExamPosition]
                listQuestionSize = currentExam.listQuestions.size
                questionAdapter.submitList(currentExam.listQuestions)
                questionAdapter.updateQuestionStateList(generateEmptyQuestionStateList(currentExam.listQuestions))
            }
        }
    }

    override fun onDestroyView() {
        viewModel.setVisibleFinishExamButton(false)
        viewModel.saveCurrentExamState()
        super.onDestroyView()
    }

    companion object {
        private const val CHANGE_TO_NEXT_QUESTION_DELAY_TIME = 700L
        private const val NO_TIME_LEFT = 0L
        private const val CENTER_TEXT_QUESTION = 0.5F
        private const val DIALOG_TITLE = "Thông báo"
        private const val DIALOG_MESSAGE = "Bạn có muốn thoát bài kiểm tra này không?"
        private const val YES_BUTTON = "Có"
        private const val NO_BUTTON = "Không"
    }
}

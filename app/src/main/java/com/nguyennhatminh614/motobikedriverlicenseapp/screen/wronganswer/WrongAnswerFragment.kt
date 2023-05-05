package com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentWrongAnswerBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.QuestionDetailAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.ViewPagerAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.BottomSheetQuestionDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.LoadingDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.showToast
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.generateEmptyQuestionStateList
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IBottomSheetListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.processQuestionOptionsList
import org.koin.androidx.viewmodel.ext.android.viewModel

class WrongAnswerFragment :
    BaseFragment<FragmentWrongAnswerBinding>(FragmentWrongAnswerBinding::inflate) {

    override val viewModel by viewModel<WrongAnswerViewModel>()

    private var listQuestionSize = AppConstant.EMPTY_SIZE

    private var initBottomSheetDisplayText = ""

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentQuestionID = questionAdapter.currentList.getOrNull(position)?.id ?: 1
                val textDisplay = "Câu ${currentQuestionID}/600"
                initBottomSheetDisplayText = textDisplay
                viewBinding.textCurrentQuestions.text = textDisplay
                bottomSheetDialog.updateCurrentQuestionText(textDisplay)
            }
        }
    }

    private val bottomSheetDialog by lazy {
        BottomSheetQuestionDialog()
    }

    private val questionAdapter by lazy {
        QuestionDetailAdapter()
    }

    override fun initData() {
        (activity as? MainActivity)?.addCallbackResetWrongAnswerButton {
            val builder = AlertDialog.Builder(context)
                .setTitle(ExamFragment.DIALOG_TITLE)
                .setMessage("Bạn có muốn hủy bỏ toàn bộ trạng thái đã lựa chọn không?")
                .setPositiveButton(
                    ExamFragment.DIALOG_POSITIVE_BUTTON_TEXT
                ) { _, _ ->
                    questionAdapter.updateQuestionStateList(generateEmptyQuestionStateList(questionAdapter.currentList))
                    viewModel.removeAllSelectedState()
                    context?.showToast("Xóa trạng thái lựa chọn thành công")
                }
                .setNegativeButton(ExamFragment.DIALOG_NEGATIVE_BUTTON_TEXT) { _, _ ->
                    //Not-op
                }
                .setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }

        viewBinding.viewPagerQuestions.adapter = questionAdapter
    }

    override fun handleEvent() {
        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateDataQuestionPos(questionPos, questionOptions)
            viewModel.updateSelectedToDatabase(questionID, questionOptions)
        }

        viewBinding.buttonNextQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                viewBinding.viewPagerQuestions.currentItem++
            }
        }

        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(onPageChangeCallback)

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem > AppConstant.FIRST_INDEX) {
                viewBinding.viewPagerQuestions.currentItem--
            }
        }

        viewBinding.bottomBar.setOnClickListener {
            context?.let { notNullContext ->
                bottomSheetDialog.initDialog(
                    initBottomSheetDisplayText,
                    notNullContext,
                    viewModel.listQuestionOptions.value,
                    viewBinding.viewPagerQuestions.currentItem)
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
                )
                bottomSheetDialog.showDialog()
            }
        }
    }

    override fun bindData() {
        viewModel.listWrongAnswer.observe(viewLifecycleOwner) {
            viewBinding.layoutVisibleWhenHaveData.isVisible = it.isNotEmpty()
            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = it.isEmpty()
        }

        viewModel.listWrongAnswerQuestion.observe(viewLifecycleOwner) {
            listQuestionSize = it.size
            questionAdapter.submitList(it)
            viewBinding.textCurrentQuestions.text = "Đang tải..."
        }

        viewModel.listQuestionOptions.observe(viewLifecycleOwner) {
            bottomSheetDialog.updateDataAdapter(it)
            questionAdapter.updateQuestionStateList(it)
        }

        viewModel.updateNewDataFromDatabase(
            object : IResponseListener<Boolean> {
                override fun onSuccess(data: Boolean) {
                    LoadingDialog.hideLoadingDialog()
                }

                override fun onError(exception: Exception?) {
                    //Not-op
                }
            }
        )
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.removeCallbackResetWrongAnswerButton()
        viewBinding.viewPagerQuestions.unregisterOnPageChangeCallback(onPageChangeCallback)
        super.onDestroyView()
    }
}

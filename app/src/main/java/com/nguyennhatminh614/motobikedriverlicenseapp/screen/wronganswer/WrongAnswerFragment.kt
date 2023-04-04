package com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer

import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentWrongAnswerBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.QuestionDetailAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.ViewPagerAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.BottomSheetQuestionDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.LoadingDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.generateEmptyQuestionStateList
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IBottomSheetListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.processQuestionOptionsList
import org.koin.androidx.viewmodel.ext.android.viewModel

class WrongAnswerFragment :
    BaseFragment<FragmentWrongAnswerBinding>(FragmentWrongAnswerBinding::inflate) {

    override val viewModel by viewModel<WrongAnswerViewModel>()

    private var listQuestionSize = AppConstant.EMPTY_SIZE

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBinding.textCurrentQuestions.text =
                    "Câu ${viewBinding.viewPagerQuestions.currentItem + 1}/${listQuestionSize}"
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
        viewBinding.viewPagerQuestions.adapter = questionAdapter
    }

    override fun handleEvent() {
        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateDataQuestionPos(questionPos, questionOptions)
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
            viewBinding.layoutVisibleWhenHaveData.isVisible = !it.isEmpty()
            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = it.isEmpty()
        }

        viewModel.listWrongAnswerQuestion.observe(viewLifecycleOwner) {
            listQuestionSize = it.size
            viewBinding.textCurrentQuestions.text =
                "Câu ${viewBinding.viewPagerQuestions.currentItem + 1}/${listQuestionSize}"

            questionAdapter.submitList(it)
            questionAdapter.updateQuestionStateList(generateEmptyQuestionStateList(it))
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
        viewBinding.viewPagerQuestions.unregisterOnPageChangeCallback(onPageChangeCallback)
        super.onDestroyView()
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentExamBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant.FIRST_INDEX
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.showToast
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.generateEmptyQuestionStateList
import kotlinx.android.synthetic.main.fragment_layout_not_found_data.view.text_not_founded
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExamFragment : BaseFragment<FragmentExamBinding>(FragmentExamBinding::inflate) {

    override val viewModel by sharedViewModel<ExamViewModel>()

    private val examAdapter by lazy { ExamAdapter() }

    override fun initData() {
        viewBinding.recyclerViewExam.adapter = examAdapter
    }

    override fun handleEvent() {
        examAdapter.registerOnClickExamButtonEvent {
            viewModel.setCurrentExam(it)
            if (viewModel.listExam.value?.get(it)?.examState == ExamState.UNDEFINED.value) {
                viewModel.setVisibleFinishExamButton(true)
            } else {
                viewModel.setVisibleFinishExamButton(false)
            }
            findNavController().navigate(R.id.action_nav_exam_to_nav_detail_exam)
        }

        viewBinding.buttonShuffle.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE)
                .setPositiveButton(
                    DIALOG_POSITIVE_BUTTON_TEXT
                ) { _, _ ->
                    viewModel.createExam {
                        showToast(MESSAGE_SUCCESS_ADD_EXAM)
                    }
                }
                .setNegativeButton(DIALOG_NEGATIVE_BUTTON_TEXT) { _, _ ->
                    //Not-op
                }
                .setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun bindData() {
        viewModel.listExam.observe(viewLifecycleOwner) {
            examAdapter.submitList(it)
            if (it.isEmpty()) {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
                if (viewBinding.layoutVisibleWhenDataIsEmpty.root.text_not_founded != null) {
                    viewBinding.layoutVisibleWhenDataIsEmpty.root.text_not_founded.text =
                        "Vui lòng tạo đề mới"
                }
            } else {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = false
            }
        }
    }

    companion object {
        const val DEFAULT_ID = 0
        const val MESSAGE_SUCCESS_ADD_EXAM = "Thêm đề thi mới thành công"
        const val DIALOG_TITLE = "Thông báo"
        const val DIALOG_MESSAGE = "Bạn có muốn tạo mới 1 đề không ?"
        const val DIALOG_POSITIVE_BUTTON_TEXT = "Có"
        const val DIALOG_NEGATIVE_BUTTON_TEXT = "Không"
    }
}

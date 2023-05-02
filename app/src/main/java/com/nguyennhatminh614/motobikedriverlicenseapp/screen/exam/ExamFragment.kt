package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.findCreateExamRuleByLicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.findCreateExamRuleByLicenseTypeString
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentExamBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.infodialog.ExamInfoDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
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

    private val currentLicenseType
        get() = arguments?.getString(AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE) ?: LicenseType.A1.type

    override fun initData() {
        (activity as? MainActivity)?.apply {
            updateTitleToolbar("Đề hạng $currentLicenseType")
            addCallbackExamInfoButton {
                ExamInfoDialog.showExamInfoDialog(this, findCreateExamRuleByLicenseTypeString(this@ExamFragment.currentLicenseType))
            }
        }
        viewBinding.recyclerViewExam.apply {
            setRecycledViewPool(RecyclerView.RecycledViewPool())
            adapter = examAdapter
        }
        viewModel.getExamByLicenseType(currentLicenseType)
    }

    override fun handleEvent() {
        examAdapter.registerOnClickExamButtonEvent {
            viewModel.setCurrentExam(it)
            if (viewModel.listExam.value?.get(it)?.examState == ExamState.UNDEFINED.value) {
                viewModel.setVisibleFinishExamButton(true)

                findNavController().navigate(
                    R.id.action_nav_exam_to_nav_detail_exam,
                    bundleOf(
                        AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE to currentLicenseType
                    )
                )
            } else {
                viewModel.setVisibleFinishExamButton(false)

                findNavController().navigate(R.id.action_nav_exam_to_nav_exam_result)
            }
        }

        viewBinding.buttonAddExam.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE)
                .setPositiveButton(
                    DIALOG_POSITIVE_BUTTON_TEXT
                ) { _, _ ->
                    viewModel.createExam(currentLicenseType) {
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
        viewModel.listExam.observe(viewLifecycleOwner) { listExam ->
            val newListExam = listExam.map { it.copy() }.toMutableList()
            if (newListExam.isEmpty()) {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
                viewBinding.layoutVisibleWhenDataIsEmpty.textNotFounded.text =
                    MESSAGE_ADD_A_NEW_EXAM
            } else {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = false
            }
            viewBinding.recyclerViewExam.recycledViewPool.clear()
            examAdapter.submitList(newListExam)
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.removeCallbackExamInfoButton()
        ExamInfoDialog.shutDownDialog()
        super.onDestroyView()
    }
    companion object {
        const val DEFAULT_ID = 0
        const val MESSAGE_SUCCESS_ADD_EXAM = "Thêm đề thi mới thành công"
        const val DIALOG_TITLE = "Thông báo"
        const val DIALOG_MESSAGE = "Bạn có muốn tạo mới 1 đề không ?"
        const val DIALOG_POSITIVE_BUTTON_TEXT = "Có"
        const val DIALOG_NEGATIVE_BUTTON_TEXT = "Không"
        const val MESSAGE_ADD_A_NEW_EXAM = "Vui lòng tạo đề mới"
    }
}

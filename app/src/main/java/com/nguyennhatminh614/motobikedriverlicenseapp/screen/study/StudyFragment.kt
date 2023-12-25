package com.nguyennhatminh614.motobikedriverlicenseapp.screen.study

import android.content.Context
import androidx.navigation.fragment.findNavController
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentStudyBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.screen.IActivityMainCallback
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StudyFragment : BaseFragment<FragmentStudyBinding>(FragmentStudyBinding::inflate) {

    override val viewModel by sharedViewModel<StudyViewModel>()

    private lateinit var iActivityMainCallback: IActivityMainCallback

    private val studyAdapter by lazy { StudyAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iActivityMainCallback = context as IActivityMainCallback
    }

    override fun initData() {
        iActivityMainCallback.showResetStudyButton()
        viewBinding.recyclerViewQuestionCategory.adapter = studyAdapter
    }

    override fun handleEvent() {
        studyAdapter.registerOnClickPositionEvent {
            viewModel.setCurrentStudyCategoryPosition(it)
            findNavController().navigate(R.id.action_nav_study_to_nav_detail_study)
        }
    }

    override fun bindData() {
        viewModel.listStudyCategory.observe(viewLifecycleOwner) {
            studyAdapter.submitList(it.map { it.copy() })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        iActivityMainCallback.hideResetStudyButton()
    }
}

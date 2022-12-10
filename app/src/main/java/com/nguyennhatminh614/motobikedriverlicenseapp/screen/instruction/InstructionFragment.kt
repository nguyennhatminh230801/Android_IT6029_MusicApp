package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores.TipsHighScoreAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores.TipsHighScoreAdapter.Companion.DEGREE_180
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class InstructionFragment :
    BaseFragment<FragmentInstructionBinding>(FragmentInstructionBinding::inflate) {

    override val viewModel by sharedViewModel<InstructionViewModel>()

    override fun initData() {
        viewModel.setVisibleInstructionIcon(false)

    }

    override fun handleEvent() {
        viewBinding.cvInstructionTheory.setOnClickListener {
            setToggleTheoryInstruction()
        }

        viewBinding.buttonSpanDetailTheoryContent.setOnClickListener {
            setToggleTheoryInstruction()
        }

        viewBinding.cvInstructionPractice.setOnClickListener {
            setTogglePracticeInstruction()
        }

        viewBinding.buttonSpanDetailPracticeContent.setOnClickListener {
            setTogglePracticeInstruction()
        }
    }

    override fun bindData() {
        //Not-op
    }

    private fun setToggleTheoryInstruction() {
        if(!viewBinding.expandableViewTheory.isVisible) {
            viewBinding.buttonSpanDetailTheoryContent.animate().rotation(360F).start()
            viewBinding.expandableViewTheory.isVisible = true
        }else{
            viewBinding.buttonSpanDetailTheoryContent.animate().rotation(180F).start()
            viewBinding.expandableViewTheory.isVisible = false
        }
    }

    private fun setTogglePracticeInstruction() {
        if(!viewBinding.expandableViewPractice.isVisible) {
            viewBinding.buttonSpanDetailPracticeContent.animate().rotation(360F).start()
            viewBinding.expandableViewPractice.isVisible = true
        }else{
            viewBinding.buttonSpanDetailPracticeContent.animate().rotation(180F).start()
            viewBinding.expandableViewPractice.isVisible = false
        }
    }
    override fun onDestroyView() {
        viewModel.setVisibleInstructionIcon(true)
        super.onDestroyView()
    }
}
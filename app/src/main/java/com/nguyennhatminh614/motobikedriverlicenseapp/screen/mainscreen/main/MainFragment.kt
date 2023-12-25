package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main

import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentMainBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainCategoriesAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.screen.IActivityMainCallback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override val viewModel: MainScreenViewModel by viewModel()

    private val mainAdapter by lazy { MainCategoriesAdapter() }

    private lateinit var iMainActivityMainCallback: IActivityMainCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iMainActivityMainCallback = context as IActivityMainCallback
    }

    override fun initData() {
        viewBinding.recyclerViewCategory.adapter = mainAdapter
    }

    override fun handleEvent() {
        mainAdapter.registerOnClickItemEvent {
            when (it.type) {
                CategoryType.EXAM ->
                    findNavController().navigate(
                        R.id.action_nav_main_to_nav_exam,
                        bundleOf(
                            AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE to viewModel.getLicenseType()?.type
                        )
                    )

                CategoryType.STUDY -> {
                    iMainActivityMainCallback.showResetStudyButton()
                    findNavController().navigate(R.id.action_nav_main_to_nav_study)
                }

                CategoryType.SIGNAL -> findNavController().navigate(R.id.action_nav_main_to_nav_traffic_sign)
                CategoryType.TIPS_HIGH_SCORE -> findNavController().navigate(R.id.action_nav_main_to_nav_tips_high_score)
                CategoryType.WRONG_ANSWER -> {
                    findNavController().navigate(R.id.action_nav_main_to_nav_wrong_answer)
                }
                CategoryType.SETTINGS -> findNavController().navigate(R.id.action_nav_main_to_nav_settings)
                CategoryType.CHANGE_LICENSE_TYPE -> findNavController().navigate(R.id.action_nav_main_to_nav_change_license_type)
                CategoryType.EXAM_GUIDE -> findNavController().navigate(R.id.action_nav_main_to_nav_instruction)
            }
        }
    }


    override fun bindData() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.categories }
            .onEach { categories ->
                mainAdapter.submitList(categories)
            }
            .launchIn(lifecycleScope)
    }
}

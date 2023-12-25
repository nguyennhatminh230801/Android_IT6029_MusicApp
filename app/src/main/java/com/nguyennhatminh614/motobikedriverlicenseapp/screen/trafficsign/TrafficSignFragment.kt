package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTrafficSignBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrafficSignFragment :
    BaseFragment<FragmentTrafficSignBinding>(FragmentTrafficSignBinding::inflate) {

    private val viewPagerAdapter by lazy {
        TrafficSignScreenAdapter()
    }

    override val viewModel by viewModel<TrafficSignViewModel>()

    override fun initData() {
        viewBinding.viewPagerTrafficSign.adapter = viewPagerAdapter
    }

    override fun handleEvent() {
        viewPagerAdapter.setItemClickEvent {
            findNavController().navigate(
                R.id.action_nav_traffic_sign_to_nav_traffic_sign_detail,
                bundleOf(AppConstant.KEY_BUNDLE_TRAFFIC_SIGN to it)
            )
        }

        viewPagerAdapter.setOnUpdateTabUiState {
            viewModel.updateTrafficSignTab(it)
        }
    }

    override fun bindData() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .filterNot { it.trafficSignTab.isEmpty() }
            .onEach { uiState ->
                viewBinding.tabLayoutTrafficSignCategory.removeAllTabs()

                uiState.trafficSignTab.forEach {
                    viewBinding.tabLayoutTrafficSignCategory
                        .addTab(viewBinding.tabLayoutTrafficSignCategory.newTab().apply {
                            text = context?.getString(it.title)
                        })
                }

                viewPagerAdapter.submitList(uiState.trafficSignTab)

                TabLayoutMediator(
                    viewBinding.tabLayoutTrafficSignCategory,
                    viewBinding.viewPagerTrafficSign
                ) { tab, position ->
                    tab.text = uiState.trafficSignTab.getOrNull(position)?.title?.let {
                        context?.getString(it)
                    }
                }.attach()
            }
            .launchIn(lifecycleScope)
    }
}

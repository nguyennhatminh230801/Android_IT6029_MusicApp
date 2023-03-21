package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSignCategory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTrafficSignBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter.ViewPagerAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrafficSignFragment :
    BaseFragment<FragmentTrafficSignBinding>(FragmentTrafficSignBinding::inflate) {

    private val listTab = listOf(
        RESTRICT_TRAFFIC_SIGNAL,
        COMMAND_TRAFFIC_SIGNAL,
        INSTRUCTION_TRAFFIC_SIGNAL,
        SUB_TRAFFIC_SIGNAL,
        WARNING_TRAFFIC_SIGNAL,
    )

    private val listCategory = listOf(
        TrafficSignCategory.RESTRICT_TRAFFIC_SIGNAL.value,
        TrafficSignCategory.COMMAND_TRAFFIC_SIGNAL.value,
        TrafficSignCategory.INSTRUCTION_TRAFFIC_SIGNAL.value,
        TrafficSignCategory.SUB_TRAFFIC_SIGNAL.value,
        TrafficSignCategory.WARNING_TRAFFIC_SIGNAL.value,
    )

    private val viewPagerAdapter by lazy {
        TrafficSignScreenAdapter()
    }

    private val listData by lazy {
        mutableListOf<MutableList<TrafficSigns>>()
    }

    override val viewModel by sharedViewModel<TrafficSignViewModel>()

    override fun initData() {
        // Not op
    }

    override fun handleEvent() {
        //Not-op
    }

    override fun bindData() {
        viewModel.listTrafficSign.observe(viewLifecycleOwner) {
            listData.clear()

            listCategory.forEach { category ->
                val data = viewModel.getListByCategory(category)
                listData.add(data ?: mutableListOf())
            }

            viewBinding.tabLayoutTrafficSignCategory.removeAllTabs()

            listTab.forEach {
                viewBinding.tabLayoutTrafficSignCategory
                    .addTab(viewBinding.tabLayoutTrafficSignCategory.newTab()
                        .apply { text = it })
            }

            viewBinding.viewPagerTrafficSign.adapter = viewPagerAdapter

            viewPagerAdapter.submitList(listData)

            viewPagerAdapter.setItemClickEvent {
                findNavController().navigate(
                    R.id.action_nav_traffic_sign_to_nav_traffic_sign_detail,
                    bundleOf(AppConstant.KEY_BUNDLE_TRAFFIC_SIGN to it)
                )
            }

            TabLayoutMediator(viewBinding.tabLayoutTrafficSignCategory,
                viewBinding.viewPagerTrafficSign) { tab, position ->
                tab.text = listTab[position]
            }.attach()
        }
    }

    companion object {
        private const val RESTRICT_TRAFFIC_SIGNAL = "Biển báo cấm"
        private const val COMMAND_TRAFFIC_SIGNAL = "Biển báo hiệu lệnh"
        private const val INSTRUCTION_TRAFFIC_SIGNAL = "Biển báo chỉ dẫn"
        private const val SUB_TRAFFIC_SIGNAL = "Biển báo phụ"
        private const val WARNING_TRAFFIC_SIGNAL = "Biển báo cảnh báo"
    }
}

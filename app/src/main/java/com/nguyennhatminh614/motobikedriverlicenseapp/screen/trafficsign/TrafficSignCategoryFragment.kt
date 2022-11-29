package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTrafficSignCategoryBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TrafficSignCategoryFragment :
    BaseFragment<FragmentTrafficSignCategoryBinding>(FragmentTrafficSignCategoryBinding::inflate) {

    private val listTrafficSignData = mutableListOf<TrafficSigns>()
    private val adapter by lazy { TrafficSignAdapter() }

    override val viewModel by sharedViewModel<TrafficSignViewModel>()

    override fun initData() {
        viewBinding.recyclerViewTrafficSign.adapter = adapter
        adapter.submitList(listTrafficSignData)
        viewBinding.recyclerViewTrafficSign.isVisible = listTrafficSignData.isEmpty().not()
        viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = listTrafficSignData.isEmpty()
    }

    override fun handleEvent() {
        //Not-op
    }

    override fun bindData() {
        //Not-op
    }

    companion object {
        fun newInstance(listData: MutableList<TrafficSigns>?) =
            TrafficSignCategoryFragment().apply {
                this.listTrafficSignData.clear()
                listData?.let { this.listTrafficSignData.addAll(it) }
            }
    }
}

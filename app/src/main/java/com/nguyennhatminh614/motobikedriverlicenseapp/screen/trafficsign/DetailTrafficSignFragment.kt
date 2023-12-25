package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentDetailTrafficSignBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailTrafficSignFragment
    : BaseFragment<FragmentDetailTrafficSignBinding>(FragmentDetailTrafficSignBinding::inflate){

    override val viewModel by viewModel<BaseViewModel>()

    private val trafficSigns by lazy {
        kotlin.runCatching {
            arguments?.get(AppConstant.KEY_BUNDLE_TRAFFIC_SIGN) as TrafficSigns
        }.getOrNull()
    }
    override fun initData() {
        trafficSigns?.let {
            viewBinding.apply {
                imageTrafficSign.loadGlideImageFromUrl(
                    root.context,
                    it.thumbnail,
                )

                textIdTrafficSign.text = it.id
                textTitleTrafficSign.text = it.title
                textDescriptionTrafficSign.text = it.description
            }
        }
    }

    override fun handleEvent() {
        // Not op
    }

    override fun bindData() {

    }
}
package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentDetailTrafficSignBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailTrafficSignFragment
    : BaseFragment<FragmentDetailTrafficSignBinding>(FragmentDetailTrafficSignBinding::inflate){

    override val viewModel by viewModel<BaseViewModel>()

    override fun initData() {
        (arguments?.get(AppConstant.KEY_BUNDLE_TRAFFIC_SIGN) as? TrafficSigns)?.let {
            viewBinding.apply {
                imageTrafficSign.loadGlideImageFromUrl(
                    root.context,
                    it.imageUrl,
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
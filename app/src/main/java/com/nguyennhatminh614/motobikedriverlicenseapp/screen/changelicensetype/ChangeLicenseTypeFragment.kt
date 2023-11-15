package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseTypeData
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentChangeLicenseTypeScreenBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeLicenseTypeFragment :
    BaseFragment<FragmentChangeLicenseTypeScreenBinding>(FragmentChangeLicenseTypeScreenBinding::inflate) {

    override val viewModel by viewModel<ChangeLicenseTypeViewModel>()

    private val changeLicenseTypeAdapter by lazy { ChangeLicenseTypeAdapter() }

    override fun initData() {
        viewBinding.recyclerViewLicenseType.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = changeLicenseTypeAdapter
        }

        changeLicenseTypeAdapter.submitList(
            enumValues<LicenseType>().map { LicenseTypeData(it) }.toList()
        )
    }

    override fun handleEvent() {
        changeLicenseTypeAdapter.registerOnClickItemEvent {
            viewModel.onChangingToNewLicenseType(it.licenseType)
        }
    }

    override fun bindData() {
        viewModel.currentLicenseType
            .flowWithLifecycle(lifecycle)
            .filterNotNull()
            .onEach { licenseType ->
                changeLicenseTypeAdapter.setCurrentLicenseType(licenseType) { selectedPosition ->
                    viewBinding.recyclerViewLicenseType.scrollToPosition(selectedPosition)
                }
            }
            .launchIn(lifecycleScope)
    }
}

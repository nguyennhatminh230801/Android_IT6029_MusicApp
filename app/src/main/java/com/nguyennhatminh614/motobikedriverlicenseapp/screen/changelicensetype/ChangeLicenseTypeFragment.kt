package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentChangeLicenseTypeScreenBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    }

    override fun handleEvent() {
        changeLicenseTypeAdapter.registerOnClickItemEvent {
            viewModel.onChangingToNewLicenseType(it.licenseType)
        }
    }

    override fun bindData() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .filterNotNull()
            .map { it.licenseTypes }
            .onEach { licenseTypes ->
                changeLicenseTypeAdapter.submitList(licenseTypes)
            }
            .map { elem -> elem.indexOfFirst { it.isSelected } }
            .onEach {
                viewBinding.recyclerViewLicenseType.scrollToPosition(it)
            }
            .launchIn(lifecycleScope)
    }
}

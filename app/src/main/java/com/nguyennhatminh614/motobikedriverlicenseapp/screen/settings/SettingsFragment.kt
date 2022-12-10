package com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings

import android.util.Log
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentSettingsBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override val viewModel by sharedViewModel<SettingsViewModel>()

    override fun initData() {
        viewBinding.switchDarkMode.isChecked = viewModel.isDarkModeOn.value ?: false
    }

    override fun handleEvent() {
        viewBinding.switchDarkMode.setOnCheckedChangeListener { compoundButton, bool ->
            if (viewBinding.switchDarkMode.isChecked.not()) {
                viewModel.turnOffDarkMode()
            } else {
                viewModel.turnOnDarkMode()
            }
        }
    }

    override fun bindData() {
        //Not-op
    }
}

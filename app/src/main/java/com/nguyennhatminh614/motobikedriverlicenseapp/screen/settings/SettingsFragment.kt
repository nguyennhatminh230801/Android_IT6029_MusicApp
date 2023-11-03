package com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentSettingsBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override val viewModel by viewModel<SettingsViewModel>()

    override fun initData() { /* no-op */ }

    override fun handleEvent() {
        viewBinding.switchDarkMode.setOnClickListener {
            viewModel.invokeChangeDarkModeState()
        }
    }

    override fun bindData() {
        viewModel.currentDarkModeState
            .flowWithLifecycle(lifecycle)
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { isDarkMode ->
                viewBinding.switchDarkMode.isChecked = isDarkMode
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) MODE_NIGHT_YES else MODE_NIGHT_NO
                )
            }
            .launchIn(lifecycleScope)
    }
}

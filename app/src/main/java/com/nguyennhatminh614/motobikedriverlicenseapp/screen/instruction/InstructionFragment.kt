package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.Context
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebViewAssetLoader
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.usecase.DarkModeUseCase
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class InstructionFragment :
    BaseFragment<FragmentInstructionBinding>(FragmentInstructionBinding::inflate) {

    override val viewModel by viewModel<InstructionViewModel>()

    override fun initData() {
        //Not implement
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        val darkModeUseCase = inject<DarkModeUseCase>().value

        viewModel.isMotorbikeLicenseType
            .flowWithLifecycle(lifecycle)
            .combine(
                darkModeUseCase.currentDarkModeState
                    .flowWithLifecycle(lifecycle)
            ) { isMotorbikeLicenseType, isDarkMode ->

                viewBinding.layoutMotorbikeInstruction.isVisible = isMotorbikeLicenseType == true
                viewBinding.webViewInstruction.isVisible = isMotorbikeLicenseType?.not() ?: false

                if (isMotorbikeLicenseType == true && isDarkMode != null) {
                    context?.let { loadCarExamInstruction(it, isDarkMode) }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun loadCarExamInstruction(context: Context, isDarkMode: Boolean){
        val assetPathHandler = WebViewAssetLoader.AssetsPathHandler(context)
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(ASSET_PATH_HANDLER_KEY, assetPathHandler)
            .build()

        viewBinding.webViewInstruction.settings.apply {
            allowContentAccess = true
            allowFileAccess = true
        }

        viewBinding.webViewInstruction.apply {
            webViewClient = LocalContentWebViewClient(
                assetLoader,
                loadStartCallback = {
                    (activity as? MainActivity)?.showLoadingDialog()
                },
                loadFinishCallback = {
                    (activity as? MainActivity)?.hideLoadingDialog()
                }
            )

            if (isDarkMode) {
                loadUrl(INSTRUCTION_WEB_URL_DARK_MODE)
            } else {
                loadUrl(INSTRUCTION_WEB_URL_LIGHT_MODE)
            }
        }
    }

    companion object {
        const val INSTRUCTION_WEB_URL_DARK_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_dark_mode.html"
        const val INSTRUCTION_WEB_URL_LIGHT_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_light_mode.html"
        const val ASSET_PATH_HANDLER_KEY = "/assets/"
    }
}
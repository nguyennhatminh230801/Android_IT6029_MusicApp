package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.Context
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebViewAssetLoader
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.getAllMotorbikeLicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
        viewModel.currentLicenseType
            .flowWithLifecycle(lifecycle)
            .map { it in getAllMotorbikeLicenseType() }
            .onEach { isMotorbikeLicenseType ->
                viewBinding.layoutMotorbikeInstruction.isVisible = isMotorbikeLicenseType
                viewBinding.webViewInstruction.isVisible = !isMotorbikeLicenseType
            }.combine(
                viewModel.currentDarkModeState.flowWithLifecycle(lifecycle)
                    .map { it ?: false }) { isMotorbikeLicenseType, isDarkMode ->

                if (!isMotorbikeLicenseType) {
                    context?.let { loadCarExamInstruction(it, isDarkMode) }
                }
            }.launchIn(lifecycleScope)
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
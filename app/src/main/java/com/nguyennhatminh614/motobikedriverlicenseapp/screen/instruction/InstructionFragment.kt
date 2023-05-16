package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.Context
import android.content.SharedPreferences
import androidx.core.view.isVisible
import androidx.webkit.WebViewAssetLoader
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.isCurrentDarkMode
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
        viewModel.isMotorbikeLicenseType.observe(viewLifecycleOwner) { isMotorbike ->
            if (isMotorbike) {
                viewBinding.layoutMotorbikeInstruction.isVisible = true
                viewBinding.webViewInstruction.isVisible = false
            } else {
                viewBinding.layoutMotorbikeInstruction.isVisible = false
                viewBinding.webViewInstruction.isVisible = true
                context?.let { loadCarExamInstruction(it) }
            }
        }
    }

    private fun loadCarExamInstruction(context: Context){
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

            if (inject<SharedPreferences>().value.isCurrentDarkMode()) {
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
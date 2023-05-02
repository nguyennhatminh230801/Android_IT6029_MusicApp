package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.content.SharedPreferences
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
        context?.let { context ->
            val assetPathHandler = WebViewAssetLoader.AssetsPathHandler(context)
            val assetLoader = WebViewAssetLoader.Builder()
                .addPathHandler(ASSET_PATH_HANDLER_KEY, assetPathHandler)
                .build()

            viewBinding.webViewInstruction.settings.apply {
                allowContentAccess = true
                allowFileAccess = true
                javaScriptEnabled = true
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
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        //Not implement
    }

    companion object {
        const val INSTRUCTION_WEB_URL_DARK_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_dark_mode.html"
        const val INSTRUCTION_WEB_URL_LIGHT_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_light_mode.html"
        const val ASSET_PATH_HANDLER_KEY = "/assets/"
    }
}
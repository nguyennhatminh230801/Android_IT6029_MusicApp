package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
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
                webViewClient = LocalContentWebViewClient(assetLoader)
                loadUrl(INSTRUCTION_WEB_URL)
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
        const val INSTRUCTION_WEB_URL = "https://appassets.androidplatform.net/assets/index.html"
        const val ASSET_PATH_HANDLER_KEY =  "/assets/"
    }
}
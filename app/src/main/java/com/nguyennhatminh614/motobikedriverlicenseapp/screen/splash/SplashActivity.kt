package com.nguyennhatminh614.motobikedriverlicenseapp.screen.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ActivitySplashBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initData(savedInstanceState: Bundle?) {
        //Not-op
    }

    override fun handleEvent() {
        supportActionBar?.hide()
    }

    override fun bindData() {
        List(MAX_STATE_DOTS) { index -> index + 1 }
            .asFlow()
            .onStart {
                viewBinding.textLoading.text = "${getString(R.string.loading)}"
            }
            .map { ".".repeat(it) }
            .onEach { state ->
                delay(TIME_DELAYS_EACH_DOTS)
                viewBinding.textLoading.text = "${getString(R.string.loading)}$state"
            }
            .onCompletion {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        private const val MAX_STATE_DOTS = 3
        private const val TIME_DELAYS_EACH_DOTS = 500L
    }
}

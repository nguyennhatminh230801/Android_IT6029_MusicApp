package com.nguyennhatminh614.motobikedriverlicenseapp.screen.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ActivitySplashBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel by viewModel<SplashViewModel>()

    override fun initData(savedInstanceState: Bundle?) {
        //Not-op
    }

    override fun handleEvent() {
        supportActionBar?.hide()
    }

    override fun bindData() {
        lifecycleScope.launch {
            viewModel.loadingText.observe(this@SplashActivity) {
                viewBinding.textLoading.text = it
            }

            viewModel.isLoadingDone.observe(this@SplashActivity) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        }

//        lifecycleScope.launch {
//            loadDataToFireBase()
//        }
    }

//    private fun loadDataToFireBase() {
//        val fireStore = get<FirebaseFirestore>().collection("question_new_ver")
//
//        val jsonString =
//            resources.openRawResource(R.raw.traffic_sign).bufferedReader().use {
//                it.readLines().joinToString(separator = "") { it.trim() }
//            }
//
//        val typeToken = object : TypeToken<MutableList<TrafficSigns>>() {}.type
//        val list = Gson().fromJson<MutableList<TrafficSigns>>(jsonString, typeToken)
//
//        list.forEach {
//            fireStore.document(it.id)
//                .set(it)
//        }
//    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.screen.splash

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Questions
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ActivitySplashBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel by viewModel<SplashViewModel>()
    val jsonString = ""
    override fun initData() {
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

    suspend fun loadDataToFireBase() {
        val fireStore = get<FirebaseFirestore>().collection(AppConstant.QUESTION_COLLECTION)

        val jsonString =
            resources.openRawResource(R.raw.bocauhoi).bufferedReader().use {
                it.readLines().joinToString(separator = "") { it.trim() }
            }

        val typeToken = object : TypeToken<MutableList<Questions>>() {}.type
        val list = Gson().fromJson<MutableList<Questions>>(jsonString, typeToken)
//        Log.d("testLoadJson", list.size.toString())
//        Log.d("testLoadJson", list[0].toString())
        list.forEach {
            fireStore.document(it.id.toString())
                .set(it)
        }
    }
}

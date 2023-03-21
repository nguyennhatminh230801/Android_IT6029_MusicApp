package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen


import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ActivityMainBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction.InstructionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.settings.SettingsViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.study.StudyViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign.TrafficSignViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer.WrongAnswerViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.network.ConnectivityObserver
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.network.InternetConnection
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val baseViewModel by viewModel<BaseViewModel>()
    val studyViewModel by viewModel<StudyViewModel>()
    val examViewModel by viewModel<ExamViewModel>()
    val settingsViewModel by viewModel<SettingsViewModel>()
    val instructionViewModel by viewModel<InstructionViewModel>()
    val trafficSignViewModel by viewModel<TrafficSignViewModel>()

    val internetConnectionObserver by inject<InternetConnection>()

    val notConnectDialog by lazy {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(DIALOG_TITLE)
            .setMessage(LOST_INTERNET_CONNECTION_DIALOG_MESSAGE)
            .setNegativeButton(LOST_INTERNET_CONNECTION_DIALOG_BUTTON) { _, _ -> }
            .create()
    }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_main,
                R.id.nav_exam,
                R.id.nav_study,
                R.id.nav_traffic_sign,
                R.id.nav_tips_high_score,
                R.id.nav_wrong_answer,
                R.id.nav_settings,
            ),
            viewBinding.drawerLayout
        )
    }

    override fun initData() {
        if (internetConnectionObserver.isOnline().not()) {
            notConnectDialog.show()
        }
    }

    override fun handleEvent() {
        viewBinding.apply {
            setSupportActionBar(appBarMain.toolbar)
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            navView.setNavigationItemSelectedListener { menuItem ->
                navController.popBackStack(R.id.nav_main, false)

                when (menuItem.itemId) {
                    R.id.nav_study -> navController.navigate(R.id.action_nav_main_to_nav_study)
                    R.id.nav_settings -> navController.navigate(R.id.action_nav_main_to_nav_settings)
                    R.id.nav_exam -> navController.navigate(R.id.action_nav_main_to_nav_exam)
                    R.id.nav_tips_high_score ->
                        navController.navigate(R.id.action_nav_main_to_nav_tips_high_score)
                    R.id.nav_traffic_sign -> navController.navigate(R.id.action_nav_main_to_nav_traffic_sign)
                    R.id.nav_wrong_answer -> navController.navigate(R.id.action_nav_main_to_nav_wrong_answer)
                }

                drawerLayout.closeDrawers()

                return@setNavigationItemSelectedListener true
            }

            appBarMain.buttonFinishExam.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle(DIALOG_TITLE)
                    .setMessage(FINISH_EXAM_DIALOG_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton(FINISH_EXAM_YES_BUTTON) { _, _ ->
                        examViewModel.processFinishExamEvent {
                            findNavController(R.id.nav_host_fragment_content_main).navigateUp()
                        }
                    }
                    .setNegativeButton(FINISH_EXAM_NO_BUTTON) { _, _ ->
                        //Not-op
                    }

                val dialog = builder.create()
                dialog.window?.attributes = WindowManager.LayoutParams().apply {
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                dialog.show()
            }

            appBarMain.buttonResetStudy.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle(DIALOG_TITLE)
                    .setMessage("Bạn có muốn reset không?")
                    .setCancelable(false)
                    .setPositiveButton(FINISH_EXAM_YES_BUTTON) { _, _ ->
                        studyViewModel.resetAllStudyCategoryState()
                    }
                    .setNegativeButton(FINISH_EXAM_NO_BUTTON) { _, _ ->
                        //Not-op
                    }

                val dialog = builder.create()
                dialog.window?.attributes = WindowManager.LayoutParams().apply {
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                dialog.show()
            }

            appBarMain.buttonInstruction.setOnClickListener {
                // gỡ các fragment trước để đến nav_main
                navController.popBackStack(R.id.nav_main, false)
                navController.navigate(R.id.action_nav_main_to_nav_instruction)
            }
        }
    }

    override fun bindData() {
        baseViewModel.isVisibleResetButton.observe(this) {
            viewBinding.appBarMain.buttonResetStudy.isVisible = it
        }

        examViewModel.isVisibleFinishExamButton.observe(this) {
            viewBinding.appBarMain.buttonFinishExam.isVisible = it
        }

        settingsViewModel.isDarkModeOn.observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        lifecycleScope.launch {
            internetConnectionObserver.observe().collect { status ->
                when (status) {
                    ConnectivityObserver.Status.AVAILABLE -> {
                        if (notConnectDialog.isShowing) {
                            notConnectDialog.hide()
                        }
                    }
                    ConnectivityObserver.Status.LOST_CONNECTION -> {
                        notConnectDialog.show()
                    }
                }
            }
        }

        instructionViewModel.isVisibleInstructionIcon.observe(this){
            viewBinding.appBarMain.buttonInstruction.isVisible = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        return if (navController.currentDestination?.id == R.id.nav_detail_exam) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        settingsViewModel.saveDarkModeState()
        super.onPause()
    }

    companion object {
        private const val DIALOG_TITLE = "Thông báo"
        private const val LOST_INTERNET_CONNECTION_DIALOG_MESSAGE = "Mất kết nối mạng"
        private const val LOST_INTERNET_CONNECTION_DIALOG_BUTTON = "OK"
        private const val FINISH_EXAM_DIALOG_MESSAGE = "Bạn có muốn kết thúc bài thi này không?"
        private const val FINISH_EXAM_YES_BUTTON = "Có"
        private const val FINISH_EXAM_NO_BUTTON = "Không"
    }
}

package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question

import android.content.Context
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentQuestionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.adapter.QuestionAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.bottomsheet.QuestionBottomSheetDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.dialog.BottomSheetQuestionDialog
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.screen.IActivityMainCallback
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate) {

    private lateinit var iActivityMainCallback: IActivityMainCallback

    override val viewModel: QuestionViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iActivityMainCallback = context as IActivityMainCallback
    }

    private val screenType: QuestionScreenType by lazy {
        provideScreenType()
    }

    open fun provideScreenType(): QuestionScreenType {
        return QuestionScreenType.WrongAnswer
    }

    private val questionAdapter by lazy { QuestionAdapter() }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.updateCurrentPage(position)
        }
    }

    override fun initData() {
        viewBinding.viewPagerQuestions.adapter = questionAdapter

        when(screenType) {
            QuestionScreenType.WrongAnswer -> {
                iActivityMainCallback.updateTitleToolbar(getString(R.string.wrong_answer_category))
            }
            else -> {

            }
        }
    }

    override fun handleEvent() {
        viewBinding.buttonNextQuestion.setOnClickListener {
            viewModel.nextPage()
        }

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            viewModel.previousPage()
        }

        questionAdapter.setOnAnswerItemClick {
            viewModel.updateSelectedAnswerState(it)
        }

        viewBinding.bottomBar.setOnClickListener {
            activity?.supportFragmentManager?.let { it1 ->
                QuestionBottomSheetDialog()
                    .setInitialState(viewModel.uiState.value)
                    .setOnSaveCurrentState {
                        viewModel.updateCurrentUiState(it)
                    }
                    .show(it1)
            }
        }
    }

    override fun bindData() {
        viewModel.updateCurrentQuestionScreenType(screenType)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.questionsItemsUiStates.isEmpty() }
            .distinctUntilChanged()
            .onEach { isEmpty ->
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = isEmpty
                viewBinding.layoutVisibleWhenHaveData.isVisible = !isEmpty
            }
            .launchIn(lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.questionsItemsUiStates }
            .distinctUntilChanged()
            .onEach {
                questionAdapter.submitList(it)
            }
            .launchIn(lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.currentPosition }
            .onEach { position ->
                viewBinding.viewPagerQuestions.currentItem = position
            }
            .onEach {
                viewBinding.buttonPreviousQuestion.isVisible = !viewModel.isFirstPage()
                viewBinding.buttonNextQuestion.isVisible = !viewModel.isLastPage()
            }
            .launchIn(lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.questionsItemsUiStates.getOrNull(it.currentPosition) }
            .filterNotNull()
            .distinctUntilChanged()
            .onEach {
                viewBinding.textCurrentQuestions.text =
                    getString(R.string.text_question_current, it.question.id, 600)
            }
            .launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onStop() {
        super.onStop()
        viewBinding.viewPagerQuestions.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    companion object {
        const val ARG_QUESTION_SCREEN_TYPE = "ARG_QUESTION_SCREEN_TYPE"
        private const val TAG = "QuestionFragment"
        fun start(context: Context?, actionId: Int, screenType: QuestionScreenType) {
            val navController = if (context is BaseActivity<*>) {
                context.findNavController(R.id.nav_host_fragment_content_main)
            } else {
                null
            }

            navController?.navigate(
                actionId,
                bundleOf(
                    ARG_QUESTION_SCREEN_TYPE to screenType
                )
            )
        }
    }
}
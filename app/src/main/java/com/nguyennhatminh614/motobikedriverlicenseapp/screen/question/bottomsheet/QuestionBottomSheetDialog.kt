package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.DialogBottomSheetQuestionsListBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter.BottomSheetQuestionListAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
class QuestionBottomSheetDialog : BottomSheetDialogFragment() {

    private val binding by lazy {
        DialogBottomSheetQuestionsListBinding.inflate(layoutInflater)
    }

    private var onSaveState: (QuestionViewModel.UiState) -> Unit = {}

    private var initializeState: QuestionViewModel.UiState? = null

    private val bottomSheetAdapter by lazy { QuestionBottomSheetItemsAdapter() }

    private val viewModel: QuestionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.setContentView(binding.root)
        dialog?.window?.attributes = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        initializeState?.let { viewModel.updateCurrentUiState(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        handleEvent()
        handleObserver()
    }

    private fun initUi() {
        binding.recyclerViewQuestionIconList.adapter = bottomSheetAdapter
    }

    private fun handleEvent() {
        binding.buttonPreviousQuestion.setOnClickListener {
            viewModel.previousPage()
        }

        binding.buttonNextQuestion.setOnClickListener {
            viewModel.nextPage()
        }

        bottomSheetAdapter.setOnItemClick {
            viewModel.moveToCurrentQuestion(it)
            dismiss()
        }
    }

    private fun handleObserver() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.questionsItemsUiStates }
            .distinctUntilChanged()
            .onEach {
                bottomSheetAdapter.submitList(it)
            }
            .launchIn(lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .map { it.questionsItemsUiStates.first { it.isCurrentQuestion } }
            .distinctUntilChanged()
            .onEach {
                binding.textCurrentQuestions.text =
                    getString(R.string.text_question_current, it.question.id, 600)
            }
            .launchIn(lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.buttonPreviousQuestion.isVisible = !it.isFirstPage
                binding.buttonNextQuestion.isVisible = !it.isLastPage
            }
            .launchIn(lifecycleScope)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onSaveState(viewModel.uiState.value)
        super.onDismiss(dialog)
    }

    fun setInitialState(uiState: QuestionViewModel.UiState) = apply {
        this.initializeState = uiState
    }

    fun setOnSaveCurrentState(onSaveState: (QuestionViewModel.UiState) -> Unit) = apply {
        this.onSaveState = onSaveState
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "QuestionBottomSheetDialog"
    }
}
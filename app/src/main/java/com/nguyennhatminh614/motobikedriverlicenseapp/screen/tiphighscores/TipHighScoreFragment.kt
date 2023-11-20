package com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores

import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTipHighScoreBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.androidx.viewmodel.ext.android.viewModel

class TipHighScoreFragment :
    BaseFragment<FragmentTipHighScoreBinding>(FragmentTipHighScoreBinding::inflate) {

    override val viewModel by viewModel<TipsHighScoreViewModel>()

    private val adapter by lazy { TipsHighScoreAdapter() }

    override fun initData() {
        viewBinding.recyclerViewTipsHighScore.adapter = adapter
    }

    override fun handleEvent() {
        adapter.setOnItemClick {
            viewModel.notifySpanStateForItem(it)
        }
    }

    override fun bindData() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onStart {
                viewBinding.recyclerViewTipsHighScore.isVisible = false
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
            }
            .map { it.tipHighScoreUiStates }
            .onEach {
                viewBinding.recyclerViewTipsHighScore.isVisible = it.isNotEmpty()
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = it.isEmpty()
                if (it.isNotEmpty()) { adapter.submitList(it) }
            }
            .launchIn(lifecycleScope)
    }
}

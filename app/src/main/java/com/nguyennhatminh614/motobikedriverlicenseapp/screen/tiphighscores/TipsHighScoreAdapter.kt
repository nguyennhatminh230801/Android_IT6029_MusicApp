package com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemTipsHighScoreLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processEndline

class TipsHighScoreAdapter :
    BaseRecyclerViewAdapter<TipsHighScoreViewModel.TipHighScoreUiState, ItemTipsHighScoreLayoutBinding, TipsHighScoreAdapter.ViewHolder>(getDiffCallBack()) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<TipsHighScoreViewModel.TipHighScoreUiState>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTipsHighScoreLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    private var onItemClick: (TipsHighScoreViewModel.TipHighScoreUiState) -> Unit = {}

    fun setOnItemClick(callback: (TipsHighScoreViewModel.TipHighScoreUiState) -> Unit) {
        this.onItemClick = callback
    }

    inner class ViewHolder(
        override val binding: ItemTipsHighScoreLayoutBinding,
    ) : BaseViewHolder<TipsHighScoreViewModel.TipHighScoreUiState, ItemTipsHighScoreLayoutBinding>(binding) {
        override fun onBindData(data: TipsHighScoreViewModel.TipHighScoreUiState) = with(binding) {
            textTipsTitle.text = data.title
            textTipsContent.text = data.content.processEndline()

            if (data.isExpanded) {
                buttonSpanDetailContent.rotation = DEGREE_0
                expandableView.isVisible = true
            } else {
                buttonSpanDetailContent.rotation = DEGREE_180
                expandableView.isVisible = false
            }
        }

        override fun onBindData(
            data: TipsHighScoreViewModel.TipHighScoreUiState,
            payloads: MutableList<Any>
        ) {
            if (payloads.contains(PAYLOAD_EXPANDED)) {
                changeSelectedState(data)
            } else {
                super.onBindData(data, payloads)
            }

            binding.root.setOnClickListener {
                binding.buttonSpanDetailContent.performClick()
            }

            binding.buttonSpanDetailContent.setOnClickListener {
                onItemClick(data)
                notifyItemChanged(adapterPosition)
            }
        }

        private fun changeSelectedState(data: TipsHighScoreViewModel.TipHighScoreUiState) {
            if (data.isExpanded) {
                binding.buttonSpanDetailContent.animate().rotation(DEGREE_0).start()
                binding.expandableView.isVisible = true
            } else {
                binding.buttonSpanDetailContent.animate().rotation(DEGREE_180).start()
                binding.expandableView.isVisible = false
            }
        }
    }

    companion object {
        const val DEGREE_0 = 0F
        const val DEGREE_180 = 180F

        private const val PAYLOAD_EXPANDED = "PAYLOAD_EXPANDED"
        fun getDiffCallBack() = object : DiffUtil.ItemCallback<TipsHighScoreViewModel.TipHighScoreUiState>() {
            override fun areItemsTheSame(
                oldItem: TipsHighScoreViewModel.TipHighScoreUiState,
                newItem: TipsHighScoreViewModel.TipHighScoreUiState,
            ): Boolean = oldItem.content == newItem.content

            override fun areContentsTheSame(
                oldItem: TipsHighScoreViewModel.TipHighScoreUiState,
                newItem: TipsHighScoreViewModel.TipHighScoreUiState,
            ): Boolean = oldItem == newItem

            override fun getChangePayload(
                oldItem: TipsHighScoreViewModel.TipHighScoreUiState,
                newItem: TipsHighScoreViewModel.TipHighScoreUiState
            ): Any? {
                return if (oldItem.isExpanded != newItem.isExpanded) {
                    PAYLOAD_EXPANDED
                } else {
                    super.getChangePayload(oldItem, newItem)
                }
            }
        }
    }
}

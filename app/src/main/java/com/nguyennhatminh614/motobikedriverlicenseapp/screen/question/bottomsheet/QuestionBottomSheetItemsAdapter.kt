package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemExamQuestionBottomSheetDialogBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.adapter.QuestionAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder

class QuestionBottomSheetItemsAdapter :
    BaseRecyclerViewAdapter<QuestionViewModel.QuestionItemUiState, ItemExamQuestionBottomSheetDialogBinding, QuestionBottomSheetItemsAdapter.ViewHolder>(QuestionAdapter.DIFF) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionViewModel.QuestionItemUiState>) {
        TODO("Not yet implemented")
    }

    private var onItemClick: (QuestionViewModel.QuestionItemUiState) -> Unit = {}

    fun setOnItemClick(onItemClick: (QuestionViewModel.QuestionItemUiState) -> Unit) = apply {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExamQuestionBottomSheetDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(
        override val binding: ItemExamQuestionBottomSheetDialogBinding,
    ) : BaseViewHolder<QuestionViewModel.QuestionItemUiState, ItemExamQuestionBottomSheetDialogBinding>(binding) {
        override fun onBindData(data: QuestionViewModel.QuestionItemUiState) {
            val context = binding.root.context

            binding.itemBadgeLayout.isVisible = data.isAnySelected
            binding.textSelectionState.text = data.selectedItem?.id?.plus(1).toString()

            binding.textQuestionIndex.text = (adapterPosition + 1).toString()

            binding.itemQuestionIcon.strokeColor = ContextCompat.getColor(
                context,
                if (data.isCurrentQuestion) R.color.primary_color
                else R.color.transparent
            )

            binding.itemQuestionIcon.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    when(data.selectedItem?.state) {
                        StateQuestionOption.INCORRECT -> R.color.red_pastel
                        StateQuestionOption.CORRECT -> R.color.green_pastel
                        else -> R.color.white
                    }
                )
            )

            binding.root.setOnClickListener {
                onItemClick(data)
            }
        }
    }
}
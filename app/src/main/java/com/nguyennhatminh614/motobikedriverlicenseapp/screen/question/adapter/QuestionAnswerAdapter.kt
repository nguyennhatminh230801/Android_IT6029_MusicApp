package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemOptionsQuestionLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getSelectedColor

class QuestionAnswerAdapter :
    BaseRecyclerViewAdapter<QuestionViewModel.QuestionAnswerUiState, ItemOptionsQuestionLayoutBinding, QuestionAnswerAdapter.ViewHolder>(
        getDiffCallback()
    ) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionViewModel.QuestionAnswerUiState>) {

    }

    private var onItemClick: (QuestionViewModel.QuestionAnswerUiState) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemOptionsQuestionLayoutBinding::inflate, parent))
    }

    fun setOnItemClick(onItemClick: (QuestionViewModel.QuestionAnswerUiState) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class ViewHolder(
        override val binding: ItemOptionsQuestionLayoutBinding
    ) : BaseViewHolder<QuestionViewModel.QuestionAnswerUiState, ItemOptionsQuestionLayoutBinding>(binding) {
        override fun onBindData(data: QuestionViewModel.QuestionAnswerUiState) = with(binding) {
            val currentDisplayPosition = adapterPosition + 1
            textQuestionOptions.text = data.questionAnswers.title
            textItemPosition.text = currentDisplayPosition.toString()

            binding.layoutQuestionItem.setCardBackgroundColor(
                when (data.state) {
                    StateQuestionOption.UNSELECTED -> getSelectedColor(UNSELECTED_COLOR)
                    StateQuestionOption.SELECTED -> getSelectedColor(SELECTED_COLOR)
                    StateQuestionOption.INCORRECT -> getSelectedColor(STATE_INCORRECT_COLOR)
                    StateQuestionOption.CORRECT -> getSelectedColor(STATE_CORRECT_COLOR)
                }
            )
            viewPositionLayout.setCardBackgroundColor(getSelectedColor(
                VIEW_POSITION_LAYOUT_BACKGROUND_COLOR
            ))
            textQuestionOptions.setTextColor(getSelectedColor(TEXT_COLOR))
            textItemPosition.setTextColor(getSelectedColor(TEXT_COLOR))

            root.setOnClickListener {
                onItemClick(data)
                notifyItemRangeChanged(0, currentList.size, PAYLOAD_SELECTED)
            }
        }

        override fun onBindData(
            data: QuestionViewModel.QuestionAnswerUiState,
            payloads: MutableList<Any>
        ) = with(binding) {
            if (payloads.contains(PAYLOAD_SELECTED)) {
                binding.layoutQuestionItem.setCardBackgroundColor(
                    when (data.state) {
                        StateQuestionOption.UNSELECTED -> getSelectedColor(UNSELECTED_COLOR)
                        StateQuestionOption.SELECTED -> getSelectedColor(SELECTED_COLOR)
                        StateQuestionOption.INCORRECT -> getSelectedColor(STATE_INCORRECT_COLOR)
                        StateQuestionOption.CORRECT -> getSelectedColor(STATE_CORRECT_COLOR)
                    }
                )
            } else {
                super.onBindData(data, payloads)
            }
        }
    }

    companion object {
        private const val UNSELECTED_COLOR = R.color.white
        private const val SELECTED_COLOR = R.color.highlight_color
        private const val TEXT_COLOR = R.color.black
        private const val STATE_INCORRECT_COLOR = R.color.red_pastel
        private const val STATE_CORRECT_COLOR = R.color.green_pastel
        private const val DARK_MODE_BACKGROUND_UNSELECTED_COLOR = R.color.grey_700
        private const val VIEW_POSITION_LAYOUT_BACKGROUND_COLOR = R.color.white

        private const val PAYLOAD_SELECTED = "PAYLOAD_SELECTED"
        fun getDiffCallback() = object : DiffUtil.ItemCallback<QuestionViewModel.QuestionAnswerUiState>() {
            override fun areItemsTheSame(
                oldItem: QuestionViewModel.QuestionAnswerUiState,
                newItem: QuestionViewModel.QuestionAnswerUiState,
            ): Boolean {
                return oldItem.questionId == newItem.questionId
            }

            override fun areContentsTheSame(
                oldItem: QuestionViewModel.QuestionAnswerUiState,
                newItem: QuestionViewModel.QuestionAnswerUiState,
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: QuestionViewModel.QuestionAnswerUiState,
                newItem: QuestionViewModel.QuestionAnswerUiState
            ): Any? {
                if (oldItem.state != newItem.state) {
                    return PAYLOAD_SELECTED
                }
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }
}
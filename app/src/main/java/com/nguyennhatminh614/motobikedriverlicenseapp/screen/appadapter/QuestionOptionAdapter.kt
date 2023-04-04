package com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemOptionsQuestionLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getResourceColor

class QuestionOptionAdapter :
    BaseRecyclerViewAdapter<QuestionOptions, ItemOptionsQuestionLayoutBinding, QuestionOptionAdapter.ViewHolder>(
        QuestionOptions.getDiffUtilCallBack()) {

    private var selectedPosition = AppConstant.NONE_POSITION
    private var isClickable = true

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionOptions>) {
        this.clickItemInterface = onClickItem
    }

    fun disableClickEvent() {
        isClickable = false
    }

    fun updateStateListWithPosition(item: MutableList<QuestionOptions>, position: Int) {
        setSingleSelection(position)
        submitList(item)
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionsQuestionLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    private fun getSelectedColor(binding: ItemOptionsQuestionLayoutBinding, color: Int) =
        binding.root.context.getResourceColor(color)

    private fun setSingleSelection(adapterPosition: Int) {
        selectedPosition = adapterPosition
    }

    companion object {
        const val SELECTED_COLOR = R.color.highlight_color
        const val UNSELECTED_COLOR = R.color.primary_color
        const val STATE_UNKNOWN_COLOR = R.color.white
        const val STATE_INCORRECT_COLOR = R.color.red_pastel
        const val STATE_CORRECT_COLOR = R.color.green_pastel
    }

    inner class ViewHolder(
        override val binding: ItemOptionsQuestionLayoutBinding,
    ) : BaseViewHolder<QuestionOptions, ItemOptionsQuestionLayoutBinding>(binding) {
        override fun onBindData(item: QuestionOptions) {
            binding.apply {
                val currentDisplayPosition = adapterPosition + 1
                textQuestionOptions.text = item.title
                textItemPosition.text = currentDisplayPosition.toString()

                if (selectedPosition == adapterPosition) {
                    binding.layoutQuestionItem.setCardBackgroundColor(getSelectedColor(binding,
                        SELECTED_COLOR))
                } else {
                    binding.layoutQuestionItem.setCardBackgroundColor(getSelectedColor(binding,
                        UNSELECTED_COLOR))
                }

                when (item.stateNumber) {
                    StateQuestionOption.UNKNOWN.type -> {
                        viewPositionLayout.setCardBackgroundColor(getSelectedColor(binding,
                            STATE_UNKNOWN_COLOR))
                        textQuestionOptions.setTextColor(getSelectedColor(binding,
                            STATE_UNKNOWN_COLOR))
                    }
                    StateQuestionOption.INCORRECT.type -> {
                        viewPositionLayout.setCardBackgroundColor(getSelectedColor(binding,
                            STATE_INCORRECT_COLOR))
                        textQuestionOptions.setTextColor(getSelectedColor(binding,
                            STATE_INCORRECT_COLOR))
                    }
                    StateQuestionOption.CORRECT.type -> {
                        viewPositionLayout.setCardBackgroundColor(getSelectedColor(binding,
                            STATE_CORRECT_COLOR))
                        textQuestionOptions.setTextColor(getSelectedColor(binding,
                            STATE_CORRECT_COLOR))
                    }
                }

                if (isClickable) {
                    root.setOnClickListener {
                        setSingleSelection(adapterPosition)
                        clickItemInterface?.let { function ->
                            function(QuestionOptions(
                                questionsID = item.questionsID,
                                position = adapterPosition,
                                title = item.title,
                                isSelected = true,
                            ))
                        }
                    }
                }
            }
        }
    }
}

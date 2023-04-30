package com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter

import android.util.Log
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
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentThemeCardColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getSelectedColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.isCurrentDarkModeByDefault

class QuestionOptionAdapter :
    BaseRecyclerViewAdapter<QuestionOptions, ItemOptionsQuestionLayoutBinding, QuestionOptionAdapter.ViewHolder>(
        QuestionOptions.getDiffUtilCallBack()
    ) {

    private var selectedPosition = AppConstant.NONE_POSITION
    private var isClickable = true

    private var correctAnswerPositionExamResult = AppConstant.NONE_POSITION
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

    private fun setSingleSelection(adapterPosition: Int) {
        selectedPosition = adapterPosition
    }

    fun updateCorrectAnswerPosition(correctAnswerPosition: Int) {
        correctAnswerPositionExamResult = correctAnswerPosition
        notifyItemChanged(correctAnswerPosition)
    }

    companion object {
        const val UNSELECTED_COLOR = R.color.white
        const val SELECTED_COLOR = R.color.highlight_color
        const val TEXT_COLOR = R.color.black
        const val STATE_INCORRECT_COLOR = R.color.red_pastel
        const val STATE_CORRECT_COLOR = R.color.green_pastel
        const val DARK_MODE_BACKGROUND_UNSELECTED_COLOR = R.color.grey_700
        const val VIEW_POSITION_LAYOUT_BACKGROUND_COLOR = R.color.white
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
                    val currentColor = when (item.stateNumber) {
                        StateQuestionOption.UNKNOWN.type -> getSelectedColor(SELECTED_COLOR)
                        StateQuestionOption.INCORRECT.type -> getSelectedColor(STATE_INCORRECT_COLOR)
                        StateQuestionOption.CORRECT.type -> getSelectedColor(STATE_CORRECT_COLOR)
                        else -> getSelectedColor(UNSELECTED_COLOR)
                    }
                    layoutQuestionItem.setCardBackgroundColor(currentColor)
                } else {
                    if(isCurrentDarkModeByDefault) {
                        layoutQuestionItem.setCardBackgroundColor(getSelectedColor(
                            DARK_MODE_BACKGROUND_UNSELECTED_COLOR))
                    } else {
                        layoutQuestionItem.setCardBackgroundColor(getSelectedColor(UNSELECTED_COLOR))
                    }
                }

                if(correctAnswerPositionExamResult != AppConstant.NONE_POSITION
                        && correctAnswerPositionExamResult == adapterPosition) {
                    layoutQuestionItem.setCardBackgroundColor(getSelectedColor(STATE_CORRECT_COLOR))
                }

                viewPositionLayout.setCardBackgroundColor(getSelectedColor(
                    VIEW_POSITION_LAYOUT_BACKGROUND_COLOR))
                textQuestionOptions.setTextColor(getSelectedColor(TEXT_COLOR))
                textItemPosition.setTextColor(getSelectedColor(TEXT_COLOR))

                if (isClickable) {
                    root.setOnClickListener {
                        setSingleSelection(adapterPosition)
                        clickItemInterface?.let { function ->
                            function(
                                QuestionOptions(
                                    questionsID = item.questionsID,
                                    position = adapterPosition,
                                    title = item.title,
                                    isSelected = true,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

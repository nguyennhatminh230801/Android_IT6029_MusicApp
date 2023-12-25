package com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.adapter

import android.text.SpannableStringBuilder
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentQuestionLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.question.QuestionViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processExplainQuestion

class QuestionAdapter : BaseRecyclerViewAdapter<QuestionViewModel.QuestionItemUiState, FragmentQuestionLayoutBinding, QuestionAdapter.ViewHolder>(
    DIFF
) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionViewModel.QuestionItemUiState>) {
        //Todo implement later
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflateViewBinding(
                FragmentQuestionLayoutBinding::inflate,
                parent
            )
        )
    }

    private var onAnswerItemClick: (QuestionViewModel.QuestionAnswerUiState) -> Unit = {}

    fun setOnAnswerItemClick(onAnswerItemClick: (QuestionViewModel.QuestionAnswerUiState) -> Unit) {
        this.onAnswerItemClick = onAnswerItemClick
    }

    inner class ViewHolder(
        override val binding: FragmentQuestionLayoutBinding
    ) : BaseViewHolder<QuestionViewModel.QuestionItemUiState, FragmentQuestionLayoutBinding>(binding) {

        override fun onBindData(data: QuestionViewModel.QuestionItemUiState) {
            val context = binding.root.context ?: return
            val questionAnswerAdapter = QuestionAnswerAdapter()
            binding.recyclerViewQuestionOptions.adapter = questionAnswerAdapter
            questionAnswerAdapter.submitList(data.answersUiState)

            binding.textTitleQuestions.text = SpannableStringBuilder().apply {
                bold { append("Câu ${adapterPosition + 1}: ") }
                append(data.question.questionTitle)
            }

            binding.imageQuestions.isVisible = data.question.bannerUrl.isNotEmpty()
            binding.imageQuestions.loadGlideImageFromUrl(context, data.question.bannerUrl)
            binding.textQuestionExplain.text = data.question.explain.processExplainQuestion()

            questionAnswerAdapter.setOnItemClick {
                onAnswerItemClick(it)
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<QuestionViewModel.QuestionItemUiState>() {
            override fun areItemsTheSame(
                oldItem: QuestionViewModel.QuestionItemUiState,
                newItem: QuestionViewModel.QuestionItemUiState,
            ): Boolean {
                return oldItem.question.id == newItem.question.id
            }

            override fun areContentsTheSame(
                oldItem: QuestionViewModel.QuestionItemUiState,
                newItem: QuestionViewModel.QuestionItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
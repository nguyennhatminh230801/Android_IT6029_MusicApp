package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.result

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemExamQuestionResultScreenBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter.QuestionOptionAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processExplainQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.processQuestionOptionsList

class ExamResultQuestionAdapter :
    BaseRecyclerViewAdapter<NewQuestion, ItemExamQuestionResultScreenBinding, ExamResultQuestionAdapter.ViewHolder>(
        NewQuestion.getDiffCallBack()
    ) {

    private val listQuestionState = mutableListOf<QuestionOptions>()

    inner class ViewHolder(binding: ItemExamQuestionResultScreenBinding) :
        BaseViewHolder<NewQuestion, ItemExamQuestionResultScreenBinding>(binding) {
        override fun onBindData(data: NewQuestion) {
            val questionOptionAdapter by lazy { QuestionOptionAdapter() }

//            binding.layoutDetailQuestion.setBackgroundColor(
//                binding.getCurrentThemeBackgroundColor()
//            )

            data.let { question ->
                binding.apply {
                    textTitleQuestions.text = "Câu ${adapterPosition + 1}: ${question.question}"
                    textQuestionExplain.text = question.explain.processExplainQuestion()

                    if (question.hasImageBanner) {
                        imageQuestions.isVisible = true
                        imageQuestions.loadGlideImageFromUrl(
                            root.context,
                            question.image
                        )
                    } else {
                        imageQuestions.isVisible = false
                    }

                    recyclerViewQuestionOptions.adapter = questionOptionAdapter

                    val listQuestionOptions = processQuestionOptionsList(
                        questionsID = question.id,
                        listString = question.listOption
                    )

                    questionOptionAdapter.submitList(listQuestionOptions)
//                    val selectedPosition = listQuestionState[adapterPosition].position
//
//                    if(selectedPosition != AppConstant.NONE_POSITION){
//                        listQuestionOptions[selectedPosition] =
//                            listQuestionState[adapterPosition].copy()
//                        questionOptionAdapter.updateStateListWithPosition(listQuestionOptions, selectedPosition)
//                    } else {
//                        questionOptionAdapter.submitList(listQuestionOptions)
//                    }

//                    if(listQuestionState[adapterPosition].stateNumber == StateQuestionOption.CORRECT.type) {
//                        viewQuestionExplain.visibility = View.VISIBLE
//                    } else {
//                        viewQuestionExplain.visibility = View.INVISIBLE
//                    }

                    questionOptionAdapter.disableClickEvent()
                }
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<NewQuestion>) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemExamQuestionResultScreenBinding::inflate, parent))
    }
}
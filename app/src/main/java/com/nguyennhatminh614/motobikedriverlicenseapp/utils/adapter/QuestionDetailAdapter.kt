package com.nguyennhatminh614.motobikedriverlicenseapp.utils.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StateQuestionOption
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentQuestionLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.appadapter.QuestionOptionAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentThemeBackgroundColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processExplainQuestion
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.processQuestionOptionsList

class QuestionDetailAdapter(
    private val isExamScreen: Boolean = false
) :
    BaseRecyclerViewAdapter<NewQuestion, FragmentQuestionLayoutBinding,
            QuestionDetailAdapter.ViewHolder>(NewQuestion.getDiffCallBack()) {

    private var onClickSelectedQuestionOption: ((Int, Int, QuestionOptions) -> Unit)? = null
    private val listQuestionState = mutableListOf<QuestionOptions>()
    private var _isDisableClickEvent = false
    private var _isEnableExplanation = true
    private var _isEnableShowCorrectAnswer = true

    fun updateQuestionStateList(listQuestionState: List<QuestionOptions>) {
        this.listQuestionState.clear()
        this.listQuestionState.addAll(listQuestionState)
        notifyDataSetChanged()
    }

    fun enableClickEvent() {
        _isDisableClickEvent = false
    }

    fun setOnClickSelectedQuestionOption(callback: (Int, Int, QuestionOptions) -> Unit) {
        this.onClickSelectedQuestionOption = callback
    }

    fun disableClickEvent() {
        _isDisableClickEvent = true
    }

    fun enableShowCorrectAnswer() {
        _isEnableShowCorrectAnswer = true
    }

    fun disableShowCorrectAnswer() {
        _isEnableShowCorrectAnswer = false
    }

    fun enableShowExplanation() {
        _isEnableExplanation = true
    }

    fun disableShowExplanation() {
        _isEnableExplanation = false
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<NewQuestion>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentQuestionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    inner class ViewHolder(
        binding: FragmentQuestionLayoutBinding,
    ) : BaseViewHolder<NewQuestion, FragmentQuestionLayoutBinding>(binding) {
        override fun onBindData(data: NewQuestion) {
            val questionOptionAdapter by lazy { QuestionOptionAdapter() }

            binding.layoutDetailQuestion.setBackgroundColor(
                binding.getCurrentThemeBackgroundColor()
            )

            data.let { question ->
                binding.apply {
                    if (isExamScreen){
                        textTitleQuestions.text = SpannableStringBuilder().apply {
                                bold { append("Câu ${adapterPosition + 1}: ") }
                                append(question.question)
                            }
                    } else {

                        textTitleQuestions.text = SpannableStringBuilder().apply {
                            bold { append("Câu ${question.id}" +
                                    "${if(question.isImmediateFailedTestWhenWrong) " [ĐIỂM LIỆT]" else ""}: ")}
                            append(question.question)
                        }
                    }

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

                    val selectedPosition = listQuestionState[adapterPosition].position
                    if(selectedPosition != AppConstant.NONE_POSITION){
                        listQuestionOptions[selectedPosition] =
                            listQuestionState[adapterPosition].copy()
                        questionOptionAdapter.updateStateListWithPosition(listQuestionOptions, selectedPosition)
                    } else {
                        questionOptionAdapter.submitList(listQuestionOptions)
                    }

                    if(_isEnableExplanation && listQuestionState[adapterPosition].stateNumber == StateQuestionOption.CORRECT.type) {
                        viewQuestionExplain.visibility = View.VISIBLE
                    } else {
                        viewQuestionExplain.visibility = View.INVISIBLE
                    }

                    if (_isDisableClickEvent) {
                        questionOptionAdapter.disableClickEvent()
                    }

                    questionOptionAdapter.registerOnClickItemEvent {
                        var newQuestionOption = it
                        val oldPos = it.position

                        if(_isEnableShowCorrectAnswer) {
                            if(it.position == question.correctAnswerPosition) {
                                newQuestionOption = it.copy(
                                    isSelected = true,
                                    stateNumber = StateQuestionOption.CORRECT.type
                                )
                            } else {
                                newQuestionOption = it.copy(
                                    isSelected = true,
                                    stateNumber = StateQuestionOption.INCORRECT.type
                                )
                            }
                        }
                        else{
                            newQuestionOption = it.copy(
                                isSelected = true,
                                stateNumber = StateQuestionOption.UNKNOWN.type
                            )
                        }

                        val newList = processQuestionOptionsList(
                            data.id,
                            data.listOption
                        ).apply {
                            this[oldPos] = newQuestionOption
                        }

                        questionOptionAdapter.updateStateListWithPosition(newList, oldPos)

                        onClickSelectedQuestionOption?.invoke(question.id, adapterPosition, newQuestionOption)
                    }
                }
            }
        }
    }
}
package com.nguyennhatminh614.motobikedriverlicenseapp.screen.wronganswer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionItemResponse
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.QuestionOptions
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.WrongAnswerRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.generateEmptyQuestionStateList
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.provideEmptyQuestionOption
import kotlinx.coroutines.launch

class WrongAnswerViewModel(
    private val wrongAnswerRepository: WrongAnswerRepository,
) : BaseViewModel() {

    private val _listAllQuestion = MutableLiveData<MutableList<QuestionItemResponse>>()

    private val _listWrongAnswerQuestion = MutableLiveData<MutableList<QuestionItemResponse>>()
    val listWrongAnswerQuestion: LiveData<MutableList<QuestionItemResponse>>
        get() = _listWrongAnswerQuestion

    private val _listWrongAnswer = MutableLiveData<MutableList<WrongAnswer>>()
    val listWrongAnswer: LiveData<MutableList<WrongAnswer>>
        get() = _listWrongAnswer

    private val _listQuestionState = MutableLiveData<MutableList<QuestionOptions>>()
    val listQuestionOptions: LiveData<MutableList<QuestionOptions>>
        get() = _listQuestionState

    init {
        fetchData()
    }

    private fun fetchData() {
        launchTask {
            val data = wrongAnswerRepository.getAllWrongAnswerQuestion()

            if (data.isNotEmpty()) {
                _listWrongAnswer.postValue(data)
                wrongAnswerRepository.getAllListQuestion(
                    object : IResponseListener<MutableList<QuestionItemResponse>> {
                        override fun onSuccess(data: MutableList<QuestionItemResponse>) {
                            val wrongAnswerList = mutableListOf<QuestionItemResponse>()
                            val listSelectedQuestionOptions = mutableListOf<QuestionOptions>()
                            _listAllQuestion.postValue(data)
                            _listWrongAnswer.value?.forEach { wrongAnswerQuestion ->
                                data.forEach {
                                    if (wrongAnswerQuestion.questionsID == it.id) {
                                        wrongAnswerList.add(it)
                                        return@forEach
                                    }
                                }
                                listSelectedQuestionOptions.add(wrongAnswerQuestion.lastSelectedState)
                            }

                            _listWrongAnswerQuestion.postValue(wrongAnswerList)
                            _listQuestionState.postValue(listSelectedQuestionOptions)
                            hideLoading()
                        }

                        override fun onError(exception: Exception?) {
                            this@WrongAnswerViewModel.exception.postValue(exception)
                            hideLoading()
                        }
                    }
                )
            }

            //hideLoading()
        }
    }

    fun updateNewDataFromDatabase(listener: IResponseListener<Boolean>) {
        launchTask {
            val data = wrongAnswerRepository.getAllWrongAnswerQuestion()
            _listWrongAnswer.postValue(data)

            val currentListQuestion = _listAllQuestion.value ?: mutableListOf()
            val newList = mutableListOf<QuestionItemResponse>()

            currentListQuestion.forEach { question ->
                data.forEach {
                    if (it.questionsID == question.id) {
                        newList.add(question)
                        return@forEach
                    }
                }
            }

            _listWrongAnswerQuestion.postValue(newList)
            _listQuestionState.postValue(generateEmptyQuestionStateList(newList))
            hideLoading()

            listener.onSuccess(true)
        }
    }

    fun updateDataQuestionPos(questionsPosition: Int, item: QuestionOptions) {
        val list = _listQuestionState.value
        list?.set(questionsPosition, item)
        _listQuestionState.value = list ?: mutableListOf()
    }

    fun updateSelectedToDatabase(questionID: Int, item: QuestionOptions) {
        viewModelScope.launch {
            var wrongAnswer = wrongAnswerRepository.findWrongAnswerQuestionByID(questionID)
            wrongAnswer = wrongAnswer?.copy(
                lastSelectedState = item
            ) ?: WrongAnswer(
                questionID,
                System.currentTimeMillis(),
                item
            )

            wrongAnswerRepository.updateWrongAnswerQuestion(wrongAnswer)
        }
    }

    fun removeAllSelectedState() {
        viewModelScope.launch {
            val listWrongAnswer = wrongAnswerRepository.getAllWrongAnswerQuestion()
            listWrongAnswer.forEach {
                wrongAnswerRepository.updateWrongAnswerQuestion(
                    it.copy(
                        lastSelectedState = provideEmptyQuestionOption(it.questionsID)
                    )
                )
            }
        }
    }

//    fun getQuestionOptionSelectedByQuestionPosition(questionsPosition: Int): QuestionOptions? {
//        return if (questionsPosition != AppConstant.NONE_POSITION) {
//            _listQuestionState.value?.get(questionsPosition)
//        } else null
//    }
}

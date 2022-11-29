package com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TipsHighScore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.TipsHighScoreRepository
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class TipsHighScoreViewModel(
    private val repository: TipsHighScoreRepository,
) : BaseViewModel() {
    private val _listTipsHighScore = MutableLiveData<MutableList<TipsHighScore>>()
    private val _exception = MutableLiveData<Exception?>()

    val listTipsHighScore: LiveData<MutableList<TipsHighScore>>
        get() = _listTipsHighScore

    private val _listStateVisible = MutableLiveData<MutableList<Boolean>>()

    val listStateVisible: LiveData<MutableList<Boolean>>
        get() = _listStateVisible

    init {
        fetchData()
    }

    private fun fetchData() {
        launchTask {
            repository.callTipsHighScoreData(
                object : IResponseListener<MutableList<TipsHighScore>> {
                    override fun onSuccess(data: MutableList<TipsHighScore>) {
                        _listTipsHighScore.postValue(data)
                        val tempStateList = mutableListOf<Boolean>().apply {
                            repeat(data.size) { add(false) }
                        }
                        _listStateVisible.postValue(tempStateList)
                        hideLoading()
                    }

                    override fun onError(exception: Exception?) {
                        _exception.postValue(exception)
                        hideLoading()
                    }
                }
            )
        }
    }

    fun onClickedChangeVisibleItemState(item: TipsHighScore) {
        val position = _listTipsHighScore.value?.indexOf(item)
        if (position != null) {
            val state = _listStateVisible.value?.get(position) ?: false
            val newList = mutableListOf<Boolean>().apply {
                _listStateVisible.value?.let {
                    addAll(it)
                    this[position] = state.not()
                }
            }
            _listStateVisible.value = newList
        }
    }
}

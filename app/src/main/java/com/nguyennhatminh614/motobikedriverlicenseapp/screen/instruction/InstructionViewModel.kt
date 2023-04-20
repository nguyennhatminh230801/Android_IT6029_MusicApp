package com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewModel

class InstructionViewModel: BaseViewModel(){
    private val _isVisibleInstructionIcon = MutableLiveData(true)

    val isVisibleInstructionIcon : LiveData<Boolean>
        get() = _isVisibleInstructionIcon

    fun setVisibleInstructionIcon(isVisible: Boolean) {
        _isVisibleInstructionIcon.value = isVisible
    }
}

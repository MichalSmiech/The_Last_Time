package com.michasoft.thelasttime.labelsEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 27.11.2023.
 */
class LabelsEditViewModel
    : ViewModel() {
    private val _actions: MutableSharedFlow<LabelsEditAction> = MutableSharedFlow()
    val actions: SharedFlow<LabelsEditAction> = _actions
    val state = MutableStateFlow(
        LabelsEditState(
            isLoading = false
        )
    )

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(LabelsEditAction.Finish)
        }
    }
}
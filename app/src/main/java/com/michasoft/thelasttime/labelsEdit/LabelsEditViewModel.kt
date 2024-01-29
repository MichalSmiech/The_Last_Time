package com.michasoft.thelasttime.labelsEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 27.11.2023.
 */
@OptIn(FlowPreview::class)
class LabelsEditViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<LabelsEditAction> = MutableSharedFlow()
    val actions: SharedFlow<LabelsEditAction> = _actions
    val state = MutableStateFlow(
        LabelsEditState(
            isLoading = false,
            labels = emptyList(),
            newLabelName = ""
        )
    )
    private val labelNameChanges = MutableSharedFlow<Pair<String, String>>()

    init {
        viewModelScope.launch {
            val labels = eventRepository.getLabels()
            state.update { it.copy(labels = labels) }
        }

        labelNameChanges.debounce(500).onEach {
            val (labelId, labelName) = it
            eventRepository.updateLabelName(labelId, labelName)
        }.launchIn(viewModelScope)
    }

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(LabelsEditAction.Finish)
        }
    }

    fun onLabelNameChange(label: Label, name: String) {
        val index = state.value.labels.indexOf(label)
        state.update {
            it.copy(labels = it.labels.toMutableList().apply {
                set(index, label.copy(name = name))
            })
        }
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                labelNameChanges.emit(Pair(label.id, name))
            }
        }
    }

    fun onNewNameChange(name: String) {
        state.update { it.copy(newLabelName = name) }
    }

    fun onNewLabelAdd(labelName: String) {
        val newLabel = Label(IdGenerator.newId(), labelName)
        state.update {
            it.copy(
                newLabelName = "",
                labels = listOf(newLabel) + it.labels
            )
        }
        viewModelScope.launch {
            eventRepository.insertLabel(label = newLabel)
        }
    }

    fun onLabelDelete(label: Label) {
        state.update {
            it.copy(labels = it.labels.toMutableList().apply {
                remove(label)
            })
        }
        viewModelScope.launch {
            eventRepository.deleteLabel(labelId = label.id)
        }
    }

    class Factory() : ViewModelProvider.Factory {
        @Inject
        lateinit var eventRepository: EventRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userSessionComponent().inject(this)
            return LabelsEditViewModel(
                eventRepository,
            ) as T
        }
    }
}
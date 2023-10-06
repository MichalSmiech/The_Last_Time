package com.michasoft.thelasttime.eventLabels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.eventLabels.model.LabelItem
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 06.10.2023.
 */
class EventLabelsViewModel(
    private val eventId: String,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventLabelsAction> = MutableSharedFlow()
    val actions: SharedFlow<EventLabelsAction> = _actions
    val state = MutableStateFlow(
        EventLabelsState(
            isLoading = true,
            labelItems = emptyList(),
            newLabelName = "",
            filterLabelName = ""
        )
    )
    private var eventLabels = emptySet<Label>()
    private var allLabels = emptyList<Label>()

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(EventLabelsAction.Finish)
        }
    }

    fun onStart() {
        viewModelScope.launch {
            refreshData()
        }
    }

    private suspend fun refreshData() {
        eventLabels = eventRepository.getEventLabels(eventId).toSet()
        allLabels = eventRepository.getLabels()
        refreshLabelItems()
    }

    private fun refreshLabelItems() {
        val labelItems = allLabels
            .filter {
                if (state.value.filterLabelName.isNotBlank())
                    it.name.contains(
                        state.value.filterLabelName,
                        ignoreCase = true
                    )
                else true
            }
            .map {
                LabelItem(
                    label = it,
                    checked = eventLabels.contains(it)
                )
            }
        state.update {
            it.copy(
                labelItems = labelItems,
                isLoading = false
            )
        }
    }

    fun changeLabelItemChecked(label: Label, checked: Boolean) {
        if (checked) {
            eventLabels = eventLabels + label
            viewModelScope.launch {
                eventRepository.insertEventLabel(eventId, label.id)
            }
        } else {
            eventLabels = eventLabels - label
            viewModelScope.launch {
                eventRepository.deleteEventLabel(eventId, label.id)
            }
        }
        refreshLabelItems()
    }

    fun addNewLabel(labelName: String) {
        val label = Label(IdGenerator.newId(), labelName)
        allLabels = listOf(label) + allLabels
        eventLabels += label
        clearFilterLabelName()
        viewModelScope.launch {
            eventRepository.insertLabel(label)
            eventRepository.insertEventLabel(eventId, label.id)
        }
    }

    private fun isLabelExistsWith(name: String): Boolean {
        return allLabels.any { it.name == name }
    }

    fun changeFilterLabelName(filterLabelName: String) {
        state.update { it.copy(filterLabelName = filterLabelName) }
        if (isLabelExistsWith(filterLabelName)) {
            clearNewLabelName()
        } else {
            changeNewLabelName(filterLabelName)
        }
        refreshLabelItems()
    }

    private fun changeNewLabelName(name: String) {
        state.update { it.copy(newLabelName = name) }
    }

    private fun clearFilterLabelName() {
        changeFilterLabelName("")
    }

    private fun clearNewLabelName() {
        changeNewLabelName("")
    }

    class Factory(private val eventId: String) : ViewModelProvider.Factory {
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
            return EventLabelsViewModel(
                eventId,
                eventRepository,
            ) as T
        }
    }
}
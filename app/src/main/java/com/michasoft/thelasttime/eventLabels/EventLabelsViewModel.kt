package com.michasoft.thelasttime.eventLabels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.eventLabels.model.LabelItem
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
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
            labelItems = emptyList()
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
        eventLabels = emptySet()
        allLabels = listOf(
            Label("1", "test"),
            Label("1", "water"),
            Label("1", "studia"),
            Label("1", "fire"),
        )
        refreshLabelItems()
    }

    private fun refreshLabelItems() {
        val labelItems = allLabels.map {
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
        } else {
            eventLabels = eventLabels - label
        }
        refreshLabelItems()
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
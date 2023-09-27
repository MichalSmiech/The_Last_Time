package com.michasoft.thelasttime.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 25.09.2023.
 */
class EventListViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventListAction> = MutableSharedFlow()
    val actions: SharedFlow<EventListAction> = _actions
    val state = MutableStateFlow(
        EventListState(
            isLoading = true,
            events = emptyList()
        )
    )
    val eventInstanceAddViewModel = EventInstanceAddViewModel(
        onSave = { eventInstance ->
            viewModelScope.launch {
                _actions.emit(EventListAction.HideEventInstanceAddBottomSheet)
                eventRepository.insertEventInstance(eventInstance)
            }
        }
    )

    init {
        eventRepository.eventsChanged.onEach {
            val events = eventRepository.getEventsWithLastInstanceTimestamp()
            state.update {
                it.copy(
                    events = events
                )
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun setupEvents() {
        val events = eventRepository.getEventsWithLastInstanceTimestamp()
        state.update {
            it.copy(
                isLoading = false,
                events = events
            )
        }
    }

    fun onStart() {
        viewModelScope.launch {
            setupEvents()
        }
    }

    fun onEventClicked(eventId: String) {
        viewModelScope.launch {
            _actions.emit(EventListAction.NavigateToEventDetails(eventId))
        }
    }

    fun onInstanceAdded(eventId: String) {
        viewModelScope.launch {
            val eventInstance = eventRepository.createEventInstance(eventId)
            eventInstanceAddViewModel.setup(eventInstance)
            _actions.emit(EventListAction.ShowEventInstanceAddBottomSheet)
        }
    }

    class Factory : ViewModelProvider.Factory {
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
            return EventListViewModel(
                eventRepository,
            ) as T
        }
    }
}
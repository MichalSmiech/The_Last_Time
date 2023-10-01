package com.michasoft.thelasttime.eventList

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddViewModel
import com.michasoft.thelasttime.model.SyncJobQueue
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
    private val eventRepository: EventRepository,
    private val syncJobQueue: SyncJobQueue,
    private val userPhotoUrl: Uri?
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventListAction> = MutableSharedFlow()
    val actions: SharedFlow<EventListAction> = _actions
    val state = MutableStateFlow(
        EventListState(
            isLoading = true,
            events = emptyList(),
            isErrorSync = false,
            isBottomSheetShowing = false,
            userPhotoUrl = userPhotoUrl
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
        syncJobQueue.changed.onEach {
            val error = syncJobQueue.isError()
            if (state.value.isErrorSync != error) {
                state.update { it.copy(isErrorSync = error) }
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
            val event = eventRepository.getEvent(eventId)
            eventInstanceAddViewModel.setup(eventInstance, event?.name ?: "")
            state.update { it.copy(isBottomSheetShowing = true) }
        }
    }

    fun onEventAdd() {
        viewModelScope.launch {
            _actions.emit(EventListAction.NavigateToEventAdd)
        }
    }

    fun menuItemClicked(item: MenuItemType) {
        viewModelScope.launch {
            when (item) {
                MenuItemType.SETTINGS -> _actions.emit(EventListAction.NavigateToSettings)
                MenuItemType.DEBUG -> _actions.emit(EventListAction.NavigateToDebug)
            }
        }
    }

    fun onBottomSheetHidden() {
        state.update { it.copy(isBottomSheetShowing = false) }
    }

    class Factory : ViewModelProvider.Factory {
        @Inject
        lateinit var eventRepository: EventRepository

        @Inject
        lateinit var syncJobQueue: SyncJobQueue

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
                syncJobQueue,
                application.userSessionComponent().getUserPhotoUrl()
            ) as T
        }
    }
}
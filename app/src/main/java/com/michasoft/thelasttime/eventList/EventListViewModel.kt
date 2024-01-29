package com.michasoft.thelasttime.eventList

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddViewModel
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.useCase.InsertEventInstanceUseCase
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
    private val userPhotoUrl: Uri?,
    private val userSessionRepository: UserSessionRepository,
    private val insertEventInstanceUseCase: InsertEventInstanceUseCase
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventListAction> = MutableSharedFlow()
    val actions: SharedFlow<EventListAction> = _actions
    val state = MutableStateFlow(
        EventListState(
            isLoading = true,
            events = emptyList(),
            isErrorSync = false,
            isBottomSheetShowing = false,
            userPhotoUrl = userPhotoUrl,
            labels = emptyList()
        )
    )
    val eventInstanceAddViewModel = EventInstanceAddViewModel(
        onSave = { eventInstance ->
            viewModelScope.launch {
                _actions.emit(EventListAction.HideEventInstanceAddBottomSheet)
                insertEventInstanceUseCase.execute(eventInstance)
            }
        }
    )

    init {
        eventRepository.eventsChanged.onEach {
            refreshEvents()
        }.launchIn(viewModelScope)

        syncJobQueue.changed.onEach {
            val error = syncJobQueue.isError()
            if (state.value.isErrorSync != error) {
                state.update { it.copy(isErrorSync = error) }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun refreshEvents() {
        val events = eventRepository.getEvents(
            withLastInstanceTimestamp = true,
            withLabels = true,
            withReminders = true
        )
        state.update {
            it.copy(
                isLoading = false,
                events = events
            )
        }
    }

    fun onStart() {
        viewModelScope.launch {
            refreshEvents()
            setupLabels()
        }
    }

    private suspend fun setupLabels() {
        val labels = eventRepository.getLabels()
        state.update { it.copy(labels = labels) }
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
                MenuItemType.EVENTS -> _actions.emit(EventListAction.CloseDrawer)
                MenuItemType.SIGNOUT -> _actions.emit(EventListAction.SignOut)
            }
        }
    }

    fun onBottomSheetHidden() {
        state.update { it.copy(isBottomSheetShowing = false) }
    }

    fun onLabelsEditClicked() {
        viewModelScope.launch {
            _actions.emit(EventListAction.NavigateToLabelsEdit())
        }
    }

    suspend fun singOut() {
        userSessionRepository.logout()
    }

    fun onAddNewLabelClicked() {
        viewModelScope.launch {
            _actions.emit(EventListAction.NavigateToLabelsEdit(withNewLabelFocus = true))
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Inject
        lateinit var eventRepository: EventRepository

        @Inject
        lateinit var syncJobQueue: SyncJobQueue

        @Inject
        lateinit var userSessionRepository: UserSessionRepository

        @Inject
        lateinit var insertEventInstanceUseCase: InsertEventInstanceUseCase

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
                application.userSessionComponent().getUserPhotoUrl(),
                userSessionRepository,
                insertEventInstanceUseCase
            ) as T
        }
    }
}
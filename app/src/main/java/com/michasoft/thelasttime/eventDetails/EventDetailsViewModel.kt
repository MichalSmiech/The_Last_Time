package com.michasoft.thelasttime.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddViewModel
import com.michasoft.thelasttime.permission.EnsurePostNotificationPermissionUseCase
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.LabelRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import com.michasoft.thelasttime.useCase.DeleteEventUseCase
import com.michasoft.thelasttime.useCase.InsertEventInstanceUseCase
import com.michasoft.thelasttime.userSessionComponent
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
 * Created by mśmiech on 21.09.2023.
 */
@OptIn(FlowPreview::class)
class EventDetailsViewModel(
    private val eventId: String,
    private val eventRepository: EventRepository,
    private val reminderRepository: ReminderRepository,
    private val insertEventInstanceUseCase: InsertEventInstanceUseCase,
    private val ensurePostNotificationPermissionUseCase: EnsurePostNotificationPermissionUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val labelRepository: LabelRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventDetailsAction> = MutableSharedFlow()
    val actions: SharedFlow<EventDetailsAction> = _actions
    val state = MutableStateFlow(
        EventDetailsState(
            isLoading = true,
            event = null,
            eventInstances = emptyList(),
            isDeleteConfirmationDialogShowing = false,
            isBottomSheetShowing = false,
            reminders = emptyList(),
            labels = emptyList()
        )
    )
    private val eventNameChanges = MutableSharedFlow<String>()
    val eventInstanceAddViewModel = EventInstanceAddViewModel(
        onSave = { eventInstance ->
            viewModelScope.launch {
                _actions.emit(EventDetailsAction.HideEventInstanceAddBottomSheet)
                insertEventInstanceUseCase.execute(eventInstance)
            }
        }
    )

    init {
        eventNameChanges.debounce(500).onEach {
            val event = eventRepository.getEvent(eventId)!!
            eventRepository.updateEvent(event.copy(name = it))
        }.launchIn(viewModelScope)

        eventRepository.eventsChanged.onEach {
            setupEvent()
        }.launchIn(viewModelScope)

        reminderRepository.remindersChanged.onEach {
            setupReminders()
        }.launchIn(viewModelScope)

        labelRepository.labelsChanged.onEach {
            setupLabels()
        }.launchIn(viewModelScope)
    }

    private suspend fun setupEvent() {
        val event = eventRepository.getEvent(eventId) ?: return
        val eventInstances = eventRepository.getEventInstances(eventId)
        val reminders = reminderRepository.getEventReminders(eventId = eventId)
        state.update {
            it.copy(
                isLoading = false,
                event = event,
                eventInstances = eventInstances,
                reminders = reminders
            )
        }
    }

    private suspend fun setupReminders() {
        val reminders = reminderRepository.getEventReminders(eventId = eventId)
        state.update {
            it.copy(
                reminders = reminders
            )
        }
    }

    private suspend fun setupLabels() {
        val labels = labelRepository.getEventLabels(eventId)
        state.update {
            it.copy(labels = labels)
        }
    }

    fun changeName(name: String) {
        state.update {
            it.copy(event = it.event!!.copy(name = name))
        }
        viewModelScope.launch {
            eventNameChanges.emit(name)
        }
    }

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(EventDetailsAction.Finish)
        }
    }

    fun onEventInstanceClicked(eventInstanceId: String) {
        viewModelScope.launch {
            _actions.emit(EventDetailsAction.NavigateToEventInstanceDetails(eventInstanceId))
        }
    }

    fun onStart() {
        viewModelScope.launch {
            setupEvent()
        }
    }

    fun onDeleteButtonClicked() {
        state.update { it.copy(isDeleteConfirmationDialogShowing = true) }
    }

    fun deleteConfirmationDialogDismissed() {
        state.update { it.copy(isDeleteConfirmationDialogShowing = false) }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            deleteEventUseCase.execute(eventId)
            _actions.emit(EventDetailsAction.Finish)
        }
    }

    fun onInstanceAdded() {
        viewModelScope.launch {
            val eventInstance = eventRepository.createEventInstance(eventId)
            val event = eventRepository.getEvent(eventId)
            eventInstanceAddViewModel.setup(eventInstance, event?.name ?: "")
            state.update { it.copy(isBottomSheetShowing = true) }
        }
    }

    fun onBottomSheetHidden() {
        state.update { it.copy(isBottomSheetShowing = false) }
    }

    fun onLabelsButtonClicked() {
        viewModelScope.launch {
            _actions.emit(EventDetailsAction.NavigateToEventLabels(eventId))
        }
    }

    fun onLabelClicked() {
        viewModelScope.launch {
            _actions.emit(EventDetailsAction.NavigateToEventLabels(eventId))
        }
    }

    fun onAddReminderButtonClicked() {
        viewModelScope.launch {
            if (ensurePostNotificationPermissionUseCase.execute()) {
                _actions.emit(EventDetailsAction.ShowEditReminderDialog(eventId))
            }
        }
    }

    fun onReminderClicked(reminderId: String) {
        viewModelScope.launch {
            _actions.emit(EventDetailsAction.ShowEditReminderDialog(eventId, reminderId))
        }
    }

    class Factory(private val eventId: String) : ViewModelProvider.Factory {
        @Inject
        lateinit var eventRepository: EventRepository

        @Inject
        lateinit var reminderRepository: ReminderRepository

        @Inject
        lateinit var insertEventInstanceUseCase: InsertEventInstanceUseCase

        @Inject
        lateinit var ensurePostNotificationPermissionUseCase: EnsurePostNotificationPermissionUseCase

        @Inject
        lateinit var deleteEventUseCase: DeleteEventUseCase

        @Inject
        lateinit var labelRepository: LabelRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userSessionComponent().inject(this)
            return EventDetailsViewModel(
                eventId,
                eventRepository,
                reminderRepository,
                insertEventInstanceUseCase,
                ensurePostNotificationPermissionUseCase,
                deleteEventUseCase,
                labelRepository
            ) as T
        }
    }
}
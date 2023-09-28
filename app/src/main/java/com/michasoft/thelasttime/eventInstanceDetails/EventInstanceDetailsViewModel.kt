package com.michasoft.thelasttime.eventInstanceDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import javax.inject.Inject

/**
 * Created by mśmiech on 22.09.2023.
 */
class EventInstanceDetailsViewModel(
    private val eventId: String,
    private val instanceId: String,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventInstanceDetailsAction> = MutableSharedFlow()
    val actions: SharedFlow<EventInstanceDetailsAction> = _actions
    val state = MutableStateFlow(
        EventInstanceDetailsState(
            isLoading = true,
            eventName = "",
            eventInstance = null,
            isDeleteConfirmationDialogShowing = false
        )
    )

    init {
        viewModelScope.launch {
            setupData()
        }
    }

    private suspend fun setupData() {
        val event = eventRepository.getEvent(eventId)!!
        val eventInstance = eventRepository.getEventInstance(eventId, instanceId) ?: return
        state.update {
            it.copy(
                isLoading = false,
                eventName = event.name,
                eventInstance = eventInstance
            )
        }
    }

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(EventInstanceDetailsAction.Finish)
        }
    }

    fun changeDate(date: LocalDate) {
        val instance = state.value.eventInstance!!.let {
            it.copy(timestamp = it.timestamp.withDate(date))
        }
        state.update {
            it.copy(eventInstance = instance)
        }
        viewModelScope.launch {
            eventRepository.updateEventInstance(instance)
        }
    }

    fun changeTime(time: LocalTime) {
        val instance = state.value.eventInstance!!.let {
            it.copy(timestamp = it.timestamp.withTime(time))
        }
        state.update {
            it.copy(eventInstance = instance)
        }
        viewModelScope.launch {
            eventRepository.updateEventInstance(instance)
        }
    }

    fun onDeleteButtonClicked() {
        state.update { it.copy(isDeleteConfirmationDialogShowing = true) }
    }

    fun deleteConfirmationDialogDismissed() {
        state.update { it.copy(isDeleteConfirmationDialogShowing = false) }
    }

    fun deleteEventInstance() {
        viewModelScope.launch {
            eventRepository.deleteEventInstance(eventId, instanceId)
            _actions.emit(EventInstanceDetailsAction.Finish)
        }
    }

    class Factory(private val eventId: String, private val instanceId: String) :
        ViewModelProvider.Factory {
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
            return EventInstanceDetailsViewModel(
                eventId,
                instanceId,
                eventRepository,
            ) as T
        }
    }
}
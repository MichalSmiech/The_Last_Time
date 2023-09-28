package com.michasoft.thelasttime.eventAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.userSessionComponent
import com.michasoft.thelasttime.util.EventFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 28.09.2023.
 */
class EventAddViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EventAddAction> = MutableSharedFlow()
    val actions: SharedFlow<EventAddAction> = _actions
    val state = MutableStateFlow(
        EventAddState(
            eventName = ""
        )
    )

    fun onSave() {
        if (state.value.eventName.isBlank()) {
            return
        }
        viewModelScope.launch {
            val event = EventFactory.createEvent(state.value.eventName)
            eventRepository.insertEvent(event)
            _actions.emit(EventAddAction.FinishAndNavigateToEventDetails(event.id))
        }
    }

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _actions.emit(EventAddAction.Finish)
        }
    }

    fun changeEventName(name: String) {
        state.update {
            it.copy(eventName = name)
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
            return EventAddViewModel(
                eventRepository,
            ) as T
        }
    }
}
package com.michasoft.thelasttime.eventInstanceAdd

import com.michasoft.thelasttime.model.EventInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.joda.time.LocalDate
import org.joda.time.LocalTime

/**
 * Created by mśmiech on 27.09.2023.
 */
class EventInstanceAddViewModel(private val onSave: (EventInstance) -> Unit) {
    val state = MutableStateFlow(
        EventInstanceAddState(
            instance = null,
            eventName = ""
        )
    )

    fun setup(instance: EventInstance, eventName: String) {
        state.update {
            it.copy(
                instance = instance,
                eventName = eventName
            )
        }
    }

    fun changeDate(date: LocalDate) {
        val instance = state.value.instance!!.let {
            it.copy(timestamp = it.timestamp.withDate(date))
        }
        state.update {
            it.copy(instance = instance)
        }
    }

    fun changeTime(time: LocalTime) {
        val instance = state.value.instance!!.let {
            it.copy(timestamp = it.timestamp.withTime(time))
        }
        state.update {
            it.copy(instance = instance)
        }
    }

    fun onSaveButtonClicked() {
        onSave(state.value.instance!!)
    }
}
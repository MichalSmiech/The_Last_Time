package com.michasoft.thelasttime.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.repo.ReminderRepository
import com.michasoft.thelasttime.userSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import javax.inject.Inject

class EditReminderViewModel(
    private val eventId: String,
    private val reminderId: String?,
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    private val _actions: MutableSharedFlow<EditReminderAction> = MutableSharedFlow()
    val actions: SharedFlow<EditReminderAction> = _actions
    val type = MutableStateFlow(Reminder.Type.Single)
    val periodText = MutableStateFlow<String>("")
    val periodTextError = periodText.map { !validatePeriodText(it) }
    val dateTime = MutableStateFlow(DateTime.now().plusHours(1))
    val dateTimeError = dateTime.map { !validateDateTime(it) }

    fun saveSingleReminder() {
        val dateTime1 = dateTime.value
        if (!validateDateTime(dateTime1)) {
            return
        }
        val reminder = SingleReminder(IdGenerator.newId(), eventId, dateTime1)
        viewModelScope.launch {
            reminderRepository.insertReminder(reminder)
            _actions.emit(EditReminderAction.Finish)
        }
    }

    private fun validateDateTime(dateTime: DateTime): Boolean {
        return dateTime.isAfter(DateTime.now())
    }

    fun changeDate(date: LocalDate) {
        this.dateTime.update { it.withDate(date) }
    }

    fun changeTime(time: LocalTime) {
        this.dateTime.update { it.withTime(time) }
    }

    fun changePeriodText(value: String) {
        periodText.update { value }
    }

    fun saveRepeatedReminder() {
        val periodText1 = periodText.value
        val validatePeriodText = validatePeriodText(periodText1)
        if (!validatePeriodText) {
            return
        }
        val reminder = RepeatedReminder(IdGenerator.newId(), eventId, periodText1)
        viewModelScope.launch {
            reminderRepository.insertReminder(reminder)
            _actions.emit(EditReminderAction.Finish)
        }
    }

    private fun validatePeriodText(periodText: String): Boolean {
        return runCatching { RepeatedReminder.getNextTrigger(periodText) }.isSuccess
    }

    fun changeType(type: Reminder.Type) {
        this.type.update { type }
    }

    class Factory(private val eventId: String, private val reminderId: String?) :
        ViewModelProvider.Factory {
        @Inject
        lateinit var reminderRepository: ReminderRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userSessionComponent().inject(this)
            return EditReminderViewModel(
                eventId,
                reminderId,
                reminderRepository,
            ) as T
        }
    }
}
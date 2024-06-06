package com.michasoft.thelasttime.reminderEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.TimeRange
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.repo.ReminderRepository
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import javax.inject.Inject

class EditReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    private lateinit var eventId: String
    private val _actions: MutableSharedFlow<EditReminderAction> = MutableSharedFlow()
    val actions: SharedFlow<EditReminderAction> = _actions
    val type = MutableStateFlow(Reminder.Type.Single)
    private val defaultPeriodText = ""
    val periodText = MutableStateFlow(defaultPeriodText)
    val periodTextError = periodText.map { !validatePeriodText(it) }
    val timeRangeEnabled = MutableStateFlow<Boolean>(false)
    val timeRangeStart = MutableStateFlow<LocalTime>(LocalTime(9, 0))
    val timeRangeEnd = MutableStateFlow<LocalTime>(LocalTime(22, 0))
    val timeRangeEndError = combine(timeRangeStart, timeRangeEnd) { start, end -> !validateTimeRange(start, end) }
    private val defaultDateTime: DateTime
        get() = DateTime.now().plusHours(1).withSecondOfMinute(0).withMillisOfSecond(0)
    val dateTime = MutableStateFlow(defaultDateTime)
    val dateTimeError = dateTime.map { !validateDateTime(it) }
    private var reminderId: String? = null
    val reshowEnabled = MutableStateFlow(true)

    fun saveReminder() {
        when (type.value) {
            Reminder.Type.Single -> saveSingleReminder()
            Reminder.Type.Repeated -> saveRepeatedReminder()
        }
    }

    private fun saveSingleReminder() {
        val dateTime1 = dateTime.value
        if (!validateDateTime(dateTime1)) {
            dateTime.value = dateTime1 //refresh dateTimeError
            return
        }
        val reminder = SingleReminder(reminderId ?: IdGenerator.newId(), eventId, dateTime1, reshowEnabled.value)
        viewModelScope.launch {
            if (reminderId == null) {
                reminderRepository.insertReminder(reminder)
            } else {
                reminderRepository.updateReminder(reminder)
            }
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

    fun changeTimeRangeStart(localTime: LocalTime) {
        timeRangeStart.update { localTime }
    }

    fun changeTimeRangeEnd(localTime: LocalTime) {
        timeRangeEnd.update { localTime }
    }

    fun changeTimeRangeEnabled(enable: Boolean) {
        timeRangeEnabled.update { enable }
    }

    private fun validateTimeRange(start: LocalTime, end: LocalTime): Boolean {
        return start.isBefore(end)
    }

    private fun saveRepeatedReminder() {
        val periodText = periodText.value
        val validatePeriodText = validatePeriodText(periodText)
        if (!validatePeriodText) {
            return
        }
        val timeRangeStart = timeRangeStart.value
        val timeRangeEnd = timeRangeEnd.value
        val timeRangeEnabled = timeRangeEnabled.value
        if (timeRangeEnabled) {
            val validateTimeRange = validateTimeRange(timeRangeStart, timeRangeEnd)
            if (!validateTimeRange) {
                return
            }
        }
        val timeRange = run {
            if (timeRangeEnabled.not()) {
                return@run null
            }
            return@run TimeRange(timeRangeStart, timeRangeEnd)
        }
        val reminder =
            RepeatedReminder(reminderId ?: IdGenerator.newId(), eventId, periodText, timeRange, reshowEnabled.value)
        viewModelScope.launch {
            if (reminderId == null) {
                reminderRepository.insertReminder(reminder)
            } else {
                reminderRepository.updateReminder(reminder)
            }
            _actions.emit(EditReminderAction.Finish)
        }
    }

    private fun validatePeriodText(periodText: String): Boolean {
        return runCatching { RepeatedReminder.getNextTrigger(periodText) }.isSuccess
    }

    fun changeType(type: Reminder.Type) {
        this.type.update { type }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            reminderId?.let { reminderId ->
                reminderRepository.deleteReminder(reminderId)
            }
            _actions.emit(EditReminderAction.Finish)
        }
    }

    fun setData(eventId: String, reminderId: String?) {
        this.eventId = eventId
        this.reminderId = reminderId
        if (reminderId == null) {
            return
        }
        viewModelScope.launch {
            val reminder = reminderRepository.getReminder(reminderId) ?: return@launch
            type.value = reminder.type
            when (reminder) {
                is SingleReminder -> {
                    dateTime.value = reminder.dateTime
                }

                is RepeatedReminder -> {
                    periodText.value = reminder.periodText
                    timeRangeEnabled.value = reminder.timeRange != null
                    reminder.timeRange?.let {
                        timeRangeStart.value = it.start
                        timeRangeEnd.value = it.end
                    }
                }
            }
        }

    }

    fun changeReshowEnabled(enabled: Boolean) {
        reshowEnabled.value = enabled
    }
}
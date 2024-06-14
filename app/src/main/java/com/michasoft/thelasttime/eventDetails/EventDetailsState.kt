package com.michasoft.thelasttime.eventDetails

import com.michasoft.thelasttime.githubWidget.CalendarModel
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.reminder.Reminder

/**
 * Created by mśmiech on 21.09.2023.
 */
data class EventDetailsState(
    val isLoading: Boolean,
    val event: Event?,
    val eventInstances: List<EventInstance>,
    val isDeleteConfirmationDialogShowing: Boolean,
    val isBottomSheetShowing: Boolean,
    val reminders: List<Reminder>,
    val labels: List<Label>,
    val activityCalendarModel: CalendarModel
)
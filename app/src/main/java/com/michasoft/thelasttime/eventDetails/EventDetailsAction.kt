package com.michasoft.thelasttime.eventDetails

/**
 * Created by mśmiech on 21.09.2023.
 */
sealed class EventDetailsAction {
    object Finish : EventDetailsAction()
    class NavigateToEventInstanceDetails(val instanceId: String) : EventDetailsAction()
    object HideEventInstanceAddBottomSheet : EventDetailsAction()
    class NavigateToEventLabels(val eventId: String) : EventDetailsAction()
    class ShowEditReminderDialog(val eventId: String, val reminderId: String? = null) :
        EventDetailsAction()
}
package com.michasoft.thelasttime.eventlist

/**
 * Created by mśmiech on 25.09.2023.
 */
sealed class EventListAction {
    class NavigateToEventDetails(val eventId: String) : EventListAction()
}
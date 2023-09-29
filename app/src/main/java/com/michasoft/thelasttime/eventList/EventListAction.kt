package com.michasoft.thelasttime.eventList

/**
 * Created by mśmiech on 25.09.2023.
 */
sealed class EventListAction {
    class NavigateToEventDetails(val eventId: String) : EventListAction()
    object ShowEventInstanceAddBottomSheet : EventListAction()
    object HideEventInstanceAddBottomSheet : EventListAction()
    object NavigateToEventAdd : EventListAction()
    object NavigateToSettings : EventListAction()
    object NavigateToDebug : EventListAction()
}
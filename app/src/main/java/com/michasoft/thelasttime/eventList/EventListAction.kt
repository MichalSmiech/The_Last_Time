package com.michasoft.thelasttime.eventList

/**
 * Created by mśmiech on 25.09.2023.
 */
sealed class EventListAction {
    class NavigateToEventDetails(val eventId: String) : EventListAction()
    object HideEventInstanceAddBottomSheet : EventListAction()
    object NavigateToEventAdd : EventListAction()
    object NavigateToSettings : EventListAction()
    object NavigateToDebug : EventListAction()
    data class NavigateToLabelsEdit(val withNewLabelFocus: Boolean = false) : EventListAction()
    object CloseDrawer : EventListAction()
    object SignOut : EventListAction()
}
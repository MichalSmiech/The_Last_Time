package com.michasoft.thelasttime.eventAdd

/**
 * Created by mśmiech on 28.09.2023.
 */
sealed class EventAddAction {
    object Finish : EventAddAction()
    class FinishAndNavigateToEventDetails(val eventId: String) : EventAddAction()
}
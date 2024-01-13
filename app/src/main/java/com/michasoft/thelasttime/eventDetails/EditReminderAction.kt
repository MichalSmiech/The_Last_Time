package com.michasoft.thelasttime.eventDetails

sealed interface EditReminderAction {
    object Finish : EditReminderAction
}
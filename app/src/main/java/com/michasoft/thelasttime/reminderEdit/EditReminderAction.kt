package com.michasoft.thelasttime.reminderEdit

sealed interface EditReminderAction {
    object Finish : EditReminderAction
}
package com.michasoft.thelasttime.model.reminder

abstract class Reminder(
    val id: String,
    val eventId: String,
    val type: Type,
    var label: String,
    val nextTriggerMillis: Long? = null
) {
    enum class Type {
        Single,
        Repeated
    }
}
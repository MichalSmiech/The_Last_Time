package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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

    companion object {
        private val labelTimeFormatter = DateTimeFormat.forPattern("HH:mm")
        private val labelDatetimeFormatter = DateTimeFormat.forPattern("dd MMM, HH:mm")
        private val labelFullDatetimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy, HH:mm")

        fun createLabel(dateTime: DateTime): String {
            if (dateTime.year != DateTime.now().year) {
                return dateTime.toString(labelFullDatetimeFormatter)
            }
            if (dateTime.dayOfYear() == DateTime.now().dayOfYear()) {
                return "Today, " + dateTime.toString(labelTimeFormatter)
            }
            if (dateTime.dayOfYear() == DateTime.now().plusDays(1).dayOfYear()) {
                return "Tomorrow, " + dateTime.toString(labelTimeFormatter)
            }
            return dateTime.toString(labelDatetimeFormatter)
        }
    }
}
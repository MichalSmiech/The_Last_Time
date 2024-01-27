package com.michasoft.thelasttime.model

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat


data class TimeRange(
    val start: LocalTime,
    val end: LocalTime
) {
    fun serialize(): String {
        return start.toString("HH:mm")
            .plus(";")
            .plus(end.toString("HH:mm"))
    }

    companion object {
        fun deserialize(value: String?): TimeRange? {
            if (value == null) {
                return null
            }
            val times = value.split(";").map {
                LocalTime.parse(it, DateTimeFormat.forPattern("HH:mm"))
            }
            return TimeRange(times[0]!!, times[1]!!)
        }
    }
}
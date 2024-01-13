package com.michasoft.thelasttime.util

import androidx.room.TypeConverter
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.reminder.Reminder
import org.joda.time.DateTime

/**
 * Created by mśmiech on 01.05.2021.
 */

class RoomConverters {
    @TypeConverter
    fun dateTimeFromTimestamp(value: Long): DateTime = DateTime(value)

    @TypeConverter
    fun dateTimeToTimestamp(dateTime: DateTime): Long = dateTime.millis

    @TypeConverter
    fun fromEventInstanceFieldType(value: EventInstanceField.Type?) = value?.toString()

    @TypeConverter
    fun toEventInstanceFieldType(value: String?) = value?.let(EventInstanceField.Type::valueOf)

    @TypeConverter
    fun toReminderType(value: String?) = value?.let(Reminder.Type::valueOf)

    @TypeConverter
    fun fromReminderType(value: Reminder.Type?) = value?.toString()
}
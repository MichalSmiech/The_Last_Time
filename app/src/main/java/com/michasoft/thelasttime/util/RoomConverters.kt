package com.michasoft.thelasttime.util

import androidx.room.TypeConverter
import com.michasoft.thelasttime.model.EventInstanceField
import org.joda.time.DateTime
import java.util.*

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
}
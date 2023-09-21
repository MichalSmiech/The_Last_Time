package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.EventInstance
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */

@Entity(tableName = EventInstanceEntity.TABLE_NAME)
class EventInstanceEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val timestamp: DateTime
) {
    constructor(instance: EventInstance): this(instance.id, instance.eventId, instance.timestamp)

    companion object {
        const val TABLE_NAME = "EventInstances"
    }
}
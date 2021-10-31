package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.EventInstance
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */

@Entity(tableName = EventInstanceEntity.TABLE_NAME)
class EventInstanceEntity(
    val timestamp: DateTime,
    val eventId: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {
    constructor(instance: EventInstance): this(instance.timestamp, instance.eventId)

    companion object {
        const val TABLE_NAME = "EventInstances"
    }
}
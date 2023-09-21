package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceSchema
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
@Entity(tableName = EventEntity.TABLE_NAME)
class EventEntity(
    @PrimaryKey
    var id: String,
    val name: String,
    val createTimestamp: DateTime
) {
    constructor(event: Event) : this(event.id, event.name, event.createTimestamp)

    fun toModel(eventInstanceSchema: EventInstanceSchema) =
        Event(id, name, createTimestamp, eventInstanceSchema)

    companion object {
        const val TABLE_NAME = "Events"
    }
}
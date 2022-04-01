package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.Event
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
@Entity(tableName = EventEntity.TABLE_NAME)
class EventEntity(
    @PrimaryKey
    var id: String,
    val displayName: String,
    val createTimestamp: DateTime
) {
    constructor(event: Event): this(event.id, event.displayName, event.createTimestamp)

    fun toModel() = Event(id, displayName, createTimestamp)

    companion object {
        const val TABLE_NAME = "Events"
    }
}
package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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

    companion object {
        const val TABLE_NAME = "EventInstances"
    }
}
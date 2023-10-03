package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mśmiech on 03.10.2023.
 */
@Entity(tableName = EventLabelEntity.TABLE_NAME)
class EventLabelEntity(
    @PrimaryKey
    val eventId: String,
    val labelId: String,
) {
    companion object {
        const val TABLE_NAME = "EventLabels"
    }
}
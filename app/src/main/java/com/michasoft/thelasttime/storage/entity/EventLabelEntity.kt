package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity

/**
 * Created by mśmiech on 03.10.2023.
 */
@Entity(tableName = EventLabelEntity.TABLE_NAME, primaryKeys = ["eventId", "labelId"])
class EventLabelEntity(
    val eventId: String,
    val labelId: String,
) {
    companion object {
        const val TABLE_NAME = "EventLabels"
    }
}
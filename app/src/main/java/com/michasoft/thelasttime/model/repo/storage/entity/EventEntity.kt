package com.michasoft.thelasttime.model.repo.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

/**
 * Created by mśmiech on 11.11.2020.
 */

@Entity(tableName = EventEntity.TABLE_NAME)
class EventEntity(
    val timestamp: DateTime,
    val eventTypeId: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {

    companion object {
        const val TABLE_NAME = "Events"
    }
}
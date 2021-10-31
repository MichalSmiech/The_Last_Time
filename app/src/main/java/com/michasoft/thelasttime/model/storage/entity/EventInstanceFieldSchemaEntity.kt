package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.michasoft.thelasttime.model.EventInstanceField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceFieldSchemaEntity.TABLE_NAME)
class EventInstanceFieldSchemaEntity(
    val eventId: Long,
    val order: Int,
    val type: EventInstanceField.Type,
    val displayTitle: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {

    companion object {
        const val TABLE_NAME = "EventInstanceFieldSchemas"
    }
}
package com.michasoft.thelasttime.model.repo.storage.entity.eventField.integerField

import androidx.room.Entity
import com.michasoft.thelasttime.model.repo.storage.entity.eventField.EventFieldEntity

/**
 * Created by mśmiech on 01.05.2021.
 */

@Entity(tableName = IntegerFieldEntity.TABLE_NAME)
class IntegerFieldEntity(
    val value: Int,
    eventId: Long,
    eventFieldId: Long
): EventFieldEntity(
    eventId,
    eventFieldId
) {
    companion object {
        const val TABLE_NAME = "IntegerFields"
    }
}
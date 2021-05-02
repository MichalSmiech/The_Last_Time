package com.michasoft.thelasttime.model.repo.storage.entity.eventField.integerField

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mśmiech on 01.05.2021.
 */

@Entity(tableName = IntegerFieldSchemeEntity.TABLE_NAME)
class IntegerFieldSchemeEntity(
    val title: String,
    @PrimaryKey
    val eventFieldId: Long
) {
    companion object {
        const val TABLE_NAME = "IntegerFieldSchemes"
    }
}
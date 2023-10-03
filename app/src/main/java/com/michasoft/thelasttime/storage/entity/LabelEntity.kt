package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 03.10.2023.
 */
@Entity(tableName = LabelEntity.TABLE_NAME)
class LabelEntity(
    @PrimaryKey
    var id: String,
    val name: String,
) {
    constructor(label: Label) : this(label.id, label.name)

    fun toModel() = Label(id, name)

    companion object {
        const val TABLE_NAME = "Labels"
    }
}
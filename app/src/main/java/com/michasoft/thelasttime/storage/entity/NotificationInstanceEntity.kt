package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.NotificationInstance

@Entity(tableName = NotificationInstanceEntity.TABLE_NAME)
class NotificationInstanceEntity(
    @PrimaryKey var notificationId: Int,
    val reminderId: String
) {
    constructor(notificationInstance: NotificationInstance) : this(
        notificationInstance.notificationId,
        notificationInstance.reminderId
    )

    fun toModel() = NotificationInstance(notificationId, reminderId)

    companion object {
        const val TABLE_NAME = "NotificationInstances"
    }
}

fun NotificationInstance.toEntity() = NotificationInstanceEntity(this)
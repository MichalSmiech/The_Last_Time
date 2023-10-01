package com.michasoft.thelasttime.storage.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.User

/**
 * Created by mśmiech on 02.05.2022.
 */
@Entity(tableName = UserEntity.TABLE_NAME)
class UserEntity(
    @PrimaryKey
    var id: String,
    var remoteId: String?,
    val displayName: String,
    var isCurrent: Boolean,
    val photoUrl: String?
) {
    constructor(user: User, isCurrent: Boolean = false) : this(
        user.id,
        user.remoteId,
        user.displayName,
        isCurrent,
        user.photoUrl?.toString()
    )

    fun toModel() = User(id, remoteId, displayName, photoUrl?.let { Uri.parse(it) })

    companion object {
        const val TABLE_NAME = "Users"
    }
}
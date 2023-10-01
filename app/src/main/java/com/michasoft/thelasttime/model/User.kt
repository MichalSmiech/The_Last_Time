package com.michasoft.thelasttime.model

import android.net.Uri
import java.util.UUID

/**
 * Created by mśmiech on 29.04.2022.
 */
class User(
    val id: String,
    var remoteId: String?,
    val displayName: String,
    val photoUrl: Uri?
) {
    override fun equals(other: Any?): Boolean {
        if(other is User) {
            return id == other.id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }
}
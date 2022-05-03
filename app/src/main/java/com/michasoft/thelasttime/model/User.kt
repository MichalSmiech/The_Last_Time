package com.michasoft.thelasttime.model

import java.util.*

/**
 * Created by mśmiech on 29.04.2022.
 */
class User(
    val id: String,
    var remoteId: String?,
    val displayName: String
) {
    override fun equals(other: Any?): Boolean {
        if(other is User) {
            return id == other.id
        }
        return super.equals(other)
    }

    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }
}
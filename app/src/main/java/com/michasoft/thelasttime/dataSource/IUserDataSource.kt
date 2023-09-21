package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.model.User

/**
 * Created by mśmiech on 02.05.2022.
 */
interface IUserDataSource {
    suspend fun insertUser(user: User)
    suspend fun getUserByRemoteId(remoteId: String): User?
    suspend fun clearCurrentUserFlag()
    suspend fun setCurrentUserFlag(userId: String)
    suspend fun getCurrentUser(): User?
}
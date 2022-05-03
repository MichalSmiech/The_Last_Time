package com.michasoft.thelasttime.model.dataSource

import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.storage.dao.UserDao
import com.michasoft.thelasttime.model.storage.entity.UserEntity

/**
 * Created by mśmiech on 02.05.2022.
 */
class RoomUserDataSource(private val userDao: UserDao): IUserDataSource {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(UserEntity(user))
    }

    override suspend fun clearCurrentUserFlag() {
        userDao.clearCurrentUserFlag()
    }

    override suspend fun setCurrentUserFlag(userId: String) {
        clearCurrentUserFlag()
        userDao.setCurrentUserFlag(userId)
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()
    }

    override suspend fun getUserByRemoteId(remoteId: String): User? {
        return userDao.getUserByRemoteId(remoteId)
    }
}
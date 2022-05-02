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
}
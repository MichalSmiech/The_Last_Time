package com.michasoft.thelasttime.model.dataSource

import com.michasoft.thelasttime.model.User

/**
 * Created by mśmiech on 02.05.2022.
 */
interface IUserDataSource {
    suspend fun insertUser(user: User)
}
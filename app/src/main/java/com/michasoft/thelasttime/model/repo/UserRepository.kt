package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.dataSource.IUserDataSource
import com.michasoft.thelasttime.model.dataSource.RoomUserDataSource

/**
 * Created by mśmiech on 29.04.2022.
 */
class UserRepository(private val userDataSource: IUserDataSource) {
    var currentUser: User? = null
    var users = ArrayList<User>()


}
package com.michasoft.thelasttime.model.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import com.michasoft.thelasttime.model.storage.entity.UserEntity

/**
 * Created by mśmiech on 02.05.2022.
 */
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)
}
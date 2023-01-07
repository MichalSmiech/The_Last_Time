package com.michasoft.thelasttime.model.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.storage.entity.UserEntity

/**
 * Created by mśmiech on 02.05.2022.
 */
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE remoteId = :remoteId")
    fun getUserByRemoteId(remoteId: String): UserEntity?

    @Query("UPDATE ${UserEntity.TABLE_NAME} SET isCurrent = 0 WHERE isCurrent = 1")
    suspend fun clearCurrentUserFlag()

    @Query("UPDATE ${UserEntity.TABLE_NAME} SET isCurrent = 1 WHERE id = :userId")
    suspend fun setCurrentUserFlag(userId: String)

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE isCurrent = 1")
    suspend fun getCurrentUser(): UserEntity?
}
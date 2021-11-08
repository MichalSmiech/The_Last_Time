package com.michasoft.thelasttime.model.repo

/**
 * Created by mśmiech on 08.11.2021.
 */
interface IBackupRepository {
    suspend fun clearLocalDatabase()
    suspend fun clearRemoteDatabase()

    suspend fun copyRemoteToLocal()
    suspend fun copyLocalToRemote()
}
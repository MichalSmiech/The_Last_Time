package com.michasoft.thelasttime.repo

/**
 * Created by mśmiech on 08.11.2021.
 */
interface IBackupRepository {
    suspend fun clearLocalDatabase()
    suspend fun clearBackup()

    suspend fun restoreBackup()
    suspend fun makeBackup()
}
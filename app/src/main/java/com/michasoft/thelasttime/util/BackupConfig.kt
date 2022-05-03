package com.michasoft.thelasttime.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by mśmiech on 08.11.2021.
 */
class BackupConfig(context: Context, private val user: User): IClosable {
    private val backupConfigDataStoreCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val Context.backupConfigDataStore by preferencesDataStore(
        name = user.id.plus("_backupConfig"),
        scope = backupConfigDataStoreCoroutineScope
    )
    private val backupConfigDataStore = context.backupConfigDataStore
    private var autoBackup: Boolean? = null
    private var defaultAutoBackup = true

    suspend fun isAutoBackup(): Boolean {
        if (autoBackup == null) {
            autoBackup =
                backupConfigDataStore.data.map { preferences -> preferences[autoBackupKey] }.first()
                    ?: defaultAutoBackup
        }
        return autoBackup!!
    }

    suspend fun setAutoBackup(autoBackup: Boolean) {
        this.autoBackup = autoBackup
        backupConfigDataStore.edit { it[autoBackupKey] = autoBackup }
    }

    override suspend fun close() {
        backupConfigDataStoreCoroutineScope.cancel()
    }

    companion object {
        private val autoBackupKey = booleanPreferencesKey("autoBackup")
    }

}
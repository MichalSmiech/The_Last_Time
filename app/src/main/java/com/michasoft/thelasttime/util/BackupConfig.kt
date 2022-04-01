package com.michasoft.thelasttime.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by mśmiech on 08.11.2021.
 */
class BackupConfig(context: Context, auth: FirebaseAuth) {
    private val Context.backupConfigDataStore by preferencesDataStore(
        name = auth.currentUser!!.uid.plus("_backupConfig")
    )
    private val backupConfigDataStore = context.backupConfigDataStore
    private var autoBackup: Boolean? = null
    private var defaultAutoBackup = true

    suspend fun isAutoBackup(): Boolean {
        if(autoBackup == null) {
            autoBackup = backupConfigDataStore.data.map { preferences -> preferences[autoBackupKey] }.first() ?: defaultAutoBackup
        }
        return autoBackup!!
    }

    suspend fun setAutoBackup(autoBackup: Boolean) {
        this.autoBackup = autoBackup
        backupConfigDataStore.edit { it[autoBackupKey] = autoBackup }
    }

    companion object {
        private val autoBackupKey = booleanPreferencesKey("autoBackup")
    }
}
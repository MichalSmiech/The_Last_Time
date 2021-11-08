package com.michasoft.thelasttime.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private var backupActive: Boolean? = null

    suspend fun isBackupActive(): Boolean {
        if(backupActive == null) {
            backupActive = backupConfigDataStore.data.map { preferences -> preferences[backupActiveKey] }.first() ?: false
        }
        return backupActive!!
    }

    suspend fun setBackupConfig(active: Boolean) {
        backupActive = active
        backupConfigDataStore.edit { it[backupActiveKey] = active }
    }

    companion object {
        private val backupActiveKey = booleanPreferencesKey("backupActive")
    }
}
package com.michasoft.thelasttime.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michasoft.thelasttime.model.storage.dao.SyncJobDao
import com.michasoft.thelasttime.model.storage.dao.UserDao
import com.michasoft.thelasttime.model.storage.entity.SyncJobEntity
import com.michasoft.thelasttime.model.storage.entity.UserEntity

/**
 * Created by mśmiech on 02.05.2022.
 */
@Database(entities = [
    UserEntity::class,
    SyncJobEntity::class
], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val syncJobDao: SyncJobDao

    companion object {
        fun build(context: Context) : UserDatabase {
            val databaseName = "UsersDatabase.db"
            return Room.databaseBuilder(context, UserDatabase::class.java, databaseName)
                .build()
        }
    }
}
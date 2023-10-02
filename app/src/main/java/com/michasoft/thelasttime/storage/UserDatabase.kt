package com.michasoft.thelasttime.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michasoft.thelasttime.storage.dao.UserDao
import com.michasoft.thelasttime.storage.entity.UserEntity

/**
 * Created by mśmiech on 02.05.2022.
 */
@Database(entities = [
    UserEntity::class,
], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        fun build(context: Context) : UserDatabase {
            val databaseName = "UsersDatabase.db"
            return Room.databaseBuilder(context, UserDatabase::class.java, databaseName)
                .build()
        }
    }
}
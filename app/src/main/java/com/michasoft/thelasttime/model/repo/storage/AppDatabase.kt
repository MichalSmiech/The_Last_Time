package com.michasoft.thelasttime.model.repo.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michasoft.thelasttime.model.repo.storage.dao.EventsDao
import com.michasoft.thelasttime.model.repo.storage.entity.EventEntity
import com.michasoft.thelasttime.model.repo.storage.entity.EventInstanceEntity

/**
 * Created by mśmiech on 11.11.2020.
 */
@Database(entities = [
    EventEntity::class,
    EventInstanceEntity::class
], version = 1, exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract val eventsDao: EventsDao

    companion object {
        fun build(context: Context) : AppDatabase {
            val databaseName = "AppDatabase.db"
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .build()
        }
    }
}
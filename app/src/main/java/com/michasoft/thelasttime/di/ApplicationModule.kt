package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.EventsRepository
import com.michasoft.thelasttime.model.repo.IEventsRepository
import dagger.Module
import dagger.Provides
import org.joda.time.DateTime
import javax.inject.Singleton

/**
 * Created by mśmiech on 22.05.2021.
 */

@Module
object ApplicationModule  {
    @Singleton
    @Provides
    fun provideEventsRepository(): IEventsRepository {
        return EventsRepository()
    }
}
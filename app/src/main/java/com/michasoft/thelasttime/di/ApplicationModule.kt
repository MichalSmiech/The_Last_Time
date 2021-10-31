package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.model.repo.MockEventRepository
import com.michasoft.thelasttime.model.repo.IEventRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by mśmiech on 22.05.2021.
 */

@Module
object ApplicationModule  {
    @Singleton
    @Provides
    fun provideEventsRepository(): IEventRepository {
        return MockEventRepository()
    }
}
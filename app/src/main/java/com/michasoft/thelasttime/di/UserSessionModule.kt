package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.repo.UserSessionRepository
import dagger.Module
import dagger.Provides

/**
 * Created by mśmiech on 29.04.2022.
 */
@Module
class UserSessionModule {
    @Provides
    @UserSessionScope
    fun provideUserSessionRepository(user: User) = UserSessionRepository(user)
}
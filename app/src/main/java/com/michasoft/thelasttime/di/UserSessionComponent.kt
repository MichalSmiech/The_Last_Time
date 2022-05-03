package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.model.User
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

/**
 * Created by mśmiech on 29.04.2022.
 */
@UserSessionScope
@Subcomponent(modules = [
    UserSessionModule::class,
])
interface UserSessionComponent {
    @Subcomponent.Builder
    interface Builder {
        fun user(@BindsInstance user: User): Builder
        fun build(): UserSessionComponent
    }

    fun getUser(): User
}
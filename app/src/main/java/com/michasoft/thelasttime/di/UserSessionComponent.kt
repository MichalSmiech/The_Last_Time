package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.repo.UserSessionRepository
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.view.EditEventActivity
import com.michasoft.thelasttime.view.EventActivity
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.view.EventListActivity
import com.michasoft.thelasttime.view.MainActivity
import com.michasoft.thelasttime.view.bottomSheet.AddEventInstanceBottomSheet
import dagger.BindsInstance
import dagger.Subcomponent

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
        fun setUser(@BindsInstance user: User): Builder
        fun build(): UserSessionComponent
    }

    fun getUser(): User
    fun getUserSessionRepository(): UserSessionRepository
    fun inject(activity: MainActivity)
    fun inject(activity: EventListActivity)
    fun inject(activity: EventInstanceActivity)
    fun inject(activity: EventActivity)
    fun inject(activity: EditEventActivity)
    fun inject(bottomSheet: AddEventInstanceBottomSheet)
    fun inject(eventSyncJob: EventSyncJob)

}
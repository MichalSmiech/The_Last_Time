package com.michasoft.thelasttime.di

import android.net.Uri
import com.michasoft.thelasttime.eventAdd.EventAddViewModel
import com.michasoft.thelasttime.eventDetails.EventDetailsViewModel
import com.michasoft.thelasttime.eventInstanceDetails.EventInstanceDetailsViewModel
import com.michasoft.thelasttime.eventLabels.EventLabelsViewModel
import com.michasoft.thelasttime.eventList.EventListViewModel
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.syncJob.EventInstanceSyncJob
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.view.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

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
    @Named("userPhotoUrl")
    fun getUserPhotoUrl(): Uri?
    fun inject(activity: MainActivity)
    fun inject(eventSyncJob: EventSyncJob)
    fun inject(eventInstanceSyncJob: EventInstanceSyncJob)
    fun inject(factory: EventDetailsViewModel.Factory)
    fun inject(factory: EventInstanceDetailsViewModel.Factory)
    fun inject(factory: EventListViewModel.Factory)
    fun inject(factory: EventAddViewModel.Factory)
    fun inject(factory: EventLabelsViewModel.Factory)

}
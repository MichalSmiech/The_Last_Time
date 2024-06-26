package com.michasoft.thelasttime.di

import android.net.Uri
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.eventAdd.EventAddViewModel
import com.michasoft.thelasttime.eventDetails.EventDetailsViewModel
import com.michasoft.thelasttime.eventInstanceDetails.EventInstanceDetailsViewModel
import com.michasoft.thelasttime.eventLabels.EventLabelsViewModel
import com.michasoft.thelasttime.eventList.EventListViewModel
import com.michasoft.thelasttime.labelsEdit.LabelsEditViewModel
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.syncJob.EventInstanceSyncJob
import com.michasoft.thelasttime.model.syncJob.EventLabelSyncJob
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.LabelSyncJob
import com.michasoft.thelasttime.model.syncJob.ReminderSyncJob
import com.michasoft.thelasttime.notification.ReminderNotificationActionBroadcastReceiver
import com.michasoft.thelasttime.reminder.ReshowRemindersWorker
import com.michasoft.thelasttime.reminder.ShowReminderBroadcastReceiver
import com.michasoft.thelasttime.reminderEdit.EditReminderViewModel
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.view.MainActivity
import com.michasoft.thelasttime.view.UserSessionActivity
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
    fun getEditReminderViewModel(): EditReminderViewModel

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
    fun inject(showReminderReceiver: ShowReminderBroadcastReceiver)
    fun inject(reminderSyncJob: ReminderSyncJob)
    fun inject(reminderNotificationActionReceiver: ReminderNotificationActionBroadcastReceiver)
    fun inject(userSessionActivity: UserSessionActivity)
    fun inject(factory: LabelsEditViewModel.Factory)
    fun inject(labelSyncJob: LabelSyncJob)
    fun inject(eventLabelSyncJob: EventLabelSyncJob)
    fun inject(reshowRemindersWorker: ReshowRemindersWorker)
    fun inject(lastTimeApplication: LastTimeApplication)

}
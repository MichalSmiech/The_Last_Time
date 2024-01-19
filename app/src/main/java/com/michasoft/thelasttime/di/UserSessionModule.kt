package com.michasoft.thelasttime.di

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.dataSource.FirestoreEventSource
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.dataSource.RoomEventSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.dataSource.SyncJobDataSource
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.repo.BackupRepository
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.UserRepository
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.storage.AppDatabase
import com.michasoft.thelasttime.util.BackupConfig
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named

/**
 * Created by mśmiech on 29.04.2022.
 */
@Module
class UserSessionModule {
    @Provides
    @UserSessionScope
    fun provideUserSessionRepository(user: User, userRepository: UserRepository) =
        UserSessionRepository(user, userRepository)

    @Provides
    @UserSessionScope
    @Named("eventCollectionRef")
    fun provideEventCollectionRef(
        firestore: FirebaseFirestore,
        user: User
    ): CollectionReference {
        return firestore.collection("users")
            .document(user.remoteId!!) //TODO co w przypadku gdy lokalne konto nie jest zlinkowane z firebase?
            .collection("events")
    }

    @Provides
    @UserSessionScope
    @Named("reminderCollectionRef")
    fun provideReminderCollectionRef(
        firestore: FirebaseFirestore,
        user: User
    ): CollectionReference {
        return firestore.collection("users")
            .document(user.remoteId!!) //TODO co w przypadku gdy lokalne konto nie jest zlinkowane z firebase?
            .collection("reminders")
    }

    @Provides
    @UserSessionScope
    fun provideRemoteEventSource(
        firestore: FirebaseFirestore,
        @Named("eventCollectionRef") eventCollectionRef: CollectionReference
    ): IRemoteEventSource {
        return FirestoreEventSource(firestore, eventCollectionRef)
    }

    @Provides
    @UserSessionScope
    fun provideAppDatabase(applicationContext: Context, user: User): AppDatabase {
        return AppDatabase.build(applicationContext, user)
    }

    @Provides
    @UserSessionScope
    fun provideLocalEventSource(appDatabase: AppDatabase): ILocalEventSource {
        return RoomEventSource(appDatabase, appDatabase.eventDao)
    }

    @Provides
    @UserSessionScope
    fun provideEventRepository(
        localSource: ILocalEventSource,
        backupConfig: BackupConfig,
        syncJobQueue: SyncJobQueue,
        syncJobQueueCoordinator: SyncJobQueueCoordinator,
        localReminderSource: RoomReminderSource
    ): EventRepository {
        return EventRepository(
            localSource,
            backupConfig,
            syncJobQueue,
            syncJobQueueCoordinator,
            localReminderSource
        )
    }

    @Provides
    @UserSessionScope
    fun provideBackupRepository(
        localSource: ILocalEventSource,
        remoteSource: IRemoteEventSource,
    ): BackupRepository {
        return BackupRepository(localSource, remoteSource)
    }

    @Provides
    @UserSessionScope
    fun provideBackupConfig(
        context: Context,
        user: User,
        userSessionRepository: UserSessionRepository
    ): BackupConfig {
        return BackupConfig(context, user).also {
            userSessionRepository.closableList.add(it)
        }
    }

    @Provides
    @UserSessionScope
    fun provideSyncJobQueue(
        syncJobDataSource: SyncJobDataSource
    ): SyncJobQueue {
        return SyncJobQueue(syncJobDataSource)
    }

    @Provides
    @UserSessionScope
    fun provideSyncJobQueueCoordinator(
        context: Context,
        syncJobQueue: SyncJobQueue
    ): SyncJobQueueCoordinator {
        return SyncJobQueueCoordinator(context, syncJobQueue)
    }

    @Provides
    @UserSessionScope
    fun provideSyncJobDataSource(
        appDatabase: AppDatabase
    ) = SyncJobDataSource(appDatabase.syncJobDao)

    @Provides
    @Named("userPhotoUrl")
    fun provideUserPhotoUrl(user: User): Uri? {
        return Firebase.auth.currentUser?.photoUrl ?: user.photoUrl
    }

    @Provides
    fun provideReminderDao(appDatabase: AppDatabase) = appDatabase.reminderDao

    @Provides
    fun provideEventDao(appDatabase: AppDatabase) = appDatabase.eventDao

    @Provides
    @UserSessionScope
    @Named("reminderChanged")
    fun provideReminderChangedFlow(): MutableSharedFlow<Unit> = MutableSharedFlow()
}


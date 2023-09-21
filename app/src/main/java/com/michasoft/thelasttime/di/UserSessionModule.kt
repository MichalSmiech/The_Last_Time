package com.michasoft.thelasttime.di

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.dataSource.FirestoreEventSource
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.dataSource.RoomEventSource
import com.michasoft.thelasttime.dataSource.SyncJobDataSource
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.repo.BackupRepository
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.UserRepository
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.storage.AppDatabase
import com.michasoft.thelasttime.storage.UserDatabase
import com.michasoft.thelasttime.util.BackupConfig
import dagger.Module
import dagger.Provides
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
        remoteSource: IRemoteEventSource,
        backupConfig: BackupConfig,
        syncJobQueue: SyncJobQueue,
        syncJobQueueCoordinator: SyncJobQueueCoordinator
    ): EventRepository {
        return EventRepository(
            localSource,
            remoteSource,
            backupConfig,
            syncJobQueue,
            syncJobQueueCoordinator
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
        userDatabase: UserDatabase
    ) = SyncJobDataSource(userDatabase.syncJobDao)
}


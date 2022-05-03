package com.michasoft.thelasttime.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.dataSource.FirestoreEventSource
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.model.dataSource.RoomEventSource
import com.michasoft.thelasttime.model.repo.*
import com.michasoft.thelasttime.model.storage.AppDatabase
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
        backupConfig: BackupConfig
    ): IEventRepository {
        return EventRepository(localSource, remoteSource, backupConfig)
    }

    @Provides
    @UserSessionScope
    fun provideBackupRepository(
        localSource: ILocalEventSource,
        remoteSource: IRemoteEventSource
    ): IBackupRepository {
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
}


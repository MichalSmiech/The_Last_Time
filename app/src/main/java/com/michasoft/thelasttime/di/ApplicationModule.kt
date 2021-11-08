package com.michasoft.thelasttime.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.model.dataSource.FirestoreEventSource
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.model.dataSource.RoomEventSource
import com.michasoft.thelasttime.model.repo.*
import com.michasoft.thelasttime.model.storage.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by mśmiech on 22.05.2021.
 */

@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore.also {
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }

    @Singleton
    @Provides
    @Named("eventCollectionRef")
    fun provideEventCollectionRef(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): CollectionReference {
        return firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("events")
    }

    @Singleton
    @Provides
    fun provideRemoteEventSource(firestore: FirebaseFirestore, @Named("eventCollectionRef") eventCollectionRef: CollectionReference): IRemoteEventSource {
        return FirestoreEventSource(firestore, eventCollectionRef)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideAppDatabase(applicationContext: Context): AppDatabase {
        return AppDatabase.build(applicationContext)
    }

    @Singleton
    @Provides
    fun provideLocalEventSource(appDatabase: AppDatabase): ILocalEventSource {
        return RoomEventSource(appDatabase.eventDao)
    }

    @Singleton
    @Provides
    fun provideEventsRepository(
        localSource: ILocalEventSource,
        remoteSource: IRemoteEventSource
    ): IEventRepository {
        return EventRepository(localSource, remoteSource)
    }
}
package com.michasoft.thelasttime.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.model.dataSource.*
import com.michasoft.thelasttime.model.repo.*
import com.michasoft.thelasttime.model.storage.AppDatabase
import com.michasoft.thelasttime.model.storage.UserDatabase
import com.michasoft.thelasttime.util.BackupConfig
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
    fun provideUserRepository(context: Context, userDataSource: IUserDataSource) =
        UserRepository(context, userDataSource)

    @Singleton
    @Provides
    fun provideUserDatabase(context: Context) = UserDatabase.build(context)

    @Singleton
    @Provides
    fun provideUserDataSource(userDatabase: UserDatabase): IUserDataSource =
        RoomUserDataSource(userDatabase.userDao)
}
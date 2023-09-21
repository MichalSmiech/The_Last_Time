package com.michasoft.thelasttime.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.dataSource.IUserDataSource
import com.michasoft.thelasttime.dataSource.RoomUserDataSource
import com.michasoft.thelasttime.repo.UserRepository
import com.michasoft.thelasttime.storage.UserDatabase
import dagger.Module
import dagger.Provides
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
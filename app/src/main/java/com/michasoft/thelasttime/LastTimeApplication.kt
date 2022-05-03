package com.michasoft.thelasttime

import com.michasoft.thelasttime.di.ApplicationComponent
import com.michasoft.thelasttime.di.DaggerApplicationComponent
import com.michasoft.thelasttime.di.UserSessionComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * Created by mśmiech on 09.05.2021.
 */
open class LastTimeApplication: DaggerApplication() {
    lateinit var applicationComponent: ApplicationComponent
    var userSessionComponent: UserSessionComponent? = null
        get() {
            val currentUser = applicationComponent.getUserRepository().currentUser
            val field1 = field
            if(field1 == null) {
                if(currentUser != null) {
                    field = applicationComponent.userSessionComponent().user(currentUser).build()
                }
            } else if(field1.getUser() != currentUser) {
                if(currentUser != null) {
                    field = applicationComponent.userSessionComponent().user(currentUser).build()
                } else {
                    field = null
                }
            }
            return field
        }

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        runBlocking {
            applicationComponent.getUserRepository().init()
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationComponent
    }
}


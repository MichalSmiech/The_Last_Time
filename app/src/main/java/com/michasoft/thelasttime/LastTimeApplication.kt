package com.michasoft.thelasttime

import android.app.Application
import com.michasoft.thelasttime.di.ApplicationComponent
import com.michasoft.thelasttime.di.DaggerApplicationComponent
import com.michasoft.thelasttime.di.UserSessionComponent
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * Created by mśmiech on 09.05.2021.
 */
open class LastTimeApplication: Application() {
    lateinit var applicationComponent: ApplicationComponent
    var userSessionComponent: UserSessionComponent? = null
        get() {
            val currentUser = applicationComponent.getUserRepository().currentUser
            val field1 = field
            if(field1 == null) {
                if(currentUser != null) {
                    field = applicationComponent.userSessionComponent().setUser(currentUser).build()
                }
            } else if(field1.getUser() != currentUser) {
                if(currentUser != null) {
                    field = applicationComponent.userSessionComponent().setUser(currentUser).build()
                } else {
                    field = null
                }
            }
            return field
        }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        runBlocking {
            applicationComponent.getUserRepository().init()
        }
    }
}


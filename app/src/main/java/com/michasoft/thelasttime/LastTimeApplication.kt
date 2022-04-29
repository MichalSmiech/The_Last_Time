package com.michasoft.thelasttime

import com.michasoft.thelasttime.di.ApplicationComponent
import com.michasoft.thelasttime.di.DaggerApplicationComponent
import com.michasoft.thelasttime.di.UserSessionComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * Created by mśmiech on 09.05.2021.
 */
open class LastTimeApplication: DaggerApplication() {
    lateinit var applicationComponent: ApplicationComponent
    var userSessionComponent: UserSessionComponent? = null
        get() {
            if(field == null) {
                val currentUser = applicationComponent.getUserRepository().currentUser
                if(currentUser != null) {
                    field = applicationComponent.userSessionComponent().user(currentUser).build()
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
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationComponent
    }
}


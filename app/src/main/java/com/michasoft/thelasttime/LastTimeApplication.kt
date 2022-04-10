package com.michasoft.thelasttime

import android.app.Application
import com.michasoft.thelasttime.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * Created by mśmiech on 09.05.2021.
 */
open class LastTimeApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
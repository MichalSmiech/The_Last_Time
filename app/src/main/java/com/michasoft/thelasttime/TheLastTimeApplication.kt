package com.michasoft.thelasttime

import android.app.Application
import com.michasoft.thelasttime.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by mśmiech on 09.05.2021.
 */
open class TheLastTimeApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }

}
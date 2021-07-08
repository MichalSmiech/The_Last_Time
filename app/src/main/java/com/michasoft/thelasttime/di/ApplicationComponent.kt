package com.michasoft.thelasttime.di

import android.content.Context
import com.michasoft.thelasttime.TheLastTimeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by mśmiech on 22.05.2021.
 */
@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidInjectionModule::class,
        EventTypeListModule::class,
        EditEventTypeModule::class,
        EventTypeModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<TheLastTimeApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
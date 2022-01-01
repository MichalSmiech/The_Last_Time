package com.michasoft.thelasttime.di

import android.content.Context
import com.michasoft.thelasttime.LastTimeApplication
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
        EventTypeModule::class,
        EventModule::class,
        ActivityModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<LastTimeApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
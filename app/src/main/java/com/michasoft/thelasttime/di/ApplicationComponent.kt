package com.michasoft.thelasttime.di

import android.content.Context
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.model.repo.UserRepository
import com.michasoft.thelasttime.view.LoginActivity
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
    ]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
    fun getUserRepository(): UserRepository

    fun userSessionComponent(): UserSessionComponent.Builder
    fun inject(activity: LoginActivity)
}
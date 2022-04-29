package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.MainActivity
import com.michasoft.thelasttime.view.EditEventActivity
import com.michasoft.thelasttime.view.EventActivity
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.view.EventListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by mśmiech on 01.11.2021.
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun editEventTypeActivity(): EditEventActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventInstanceActivity(): EventInstanceActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventTypeListActivity(): EventListActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventTypeActivity(): EventActivity
}
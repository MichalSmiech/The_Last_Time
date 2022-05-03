package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.MainActivity
import com.michasoft.thelasttime.view.*
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
    internal abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun editEventActivity(): EditEventActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventInstanceActivity(): EventInstanceActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventListActivity(): EventListActivity

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventActivity(): EventActivity
}
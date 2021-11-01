package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.MainActivity
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
}
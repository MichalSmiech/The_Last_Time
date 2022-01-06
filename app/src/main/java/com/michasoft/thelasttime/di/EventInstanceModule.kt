package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class EventInstanceModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventInstanceActivity(): EventInstanceActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventInstanceViewModel::class)
    abstract fun bindViewModel(instanceViewModel: EventInstanceViewModel): ViewModel
}
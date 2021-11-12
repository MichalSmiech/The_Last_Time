package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class EventModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventActivity(): EventInstanceActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventInstanceViewModel::class)
    abstract fun bindViewModel(instanceViewModel: EventInstanceViewModel): ViewModel
}
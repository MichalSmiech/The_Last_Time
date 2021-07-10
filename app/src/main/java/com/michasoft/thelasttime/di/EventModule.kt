package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventActivity
import com.michasoft.thelasttime.view.EventTypeActivity
import com.michasoft.thelasttime.viewModel.EventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class EventModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventActivity(): EventActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindViewModel(viewModel: EventViewModel): ViewModel
}
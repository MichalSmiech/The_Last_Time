package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventActivity
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by mśmiech on 08.07.2021.
 */
@Module
abstract class EventTypeModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventTypeActivity(): EventActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindViewModel(viewModel: EventViewModel): ViewModel
}
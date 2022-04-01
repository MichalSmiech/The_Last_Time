package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventListActivity
import com.michasoft.thelasttime.viewModel.EventListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by mśmiech on 23.05.2021.
 */

@Module
abstract class EventTypeListModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun eventTypeListActivity(): EventListActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    abstract fun bindViewModel(viewModel: EventListViewModel): ViewModel
}
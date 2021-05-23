package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventTypeListActivity
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
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
    internal abstract fun eventTypeListActivity(): EventTypeListActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventTypeListViewModel::class)
    abstract fun bindViewModel(viewModel: EventTypeListViewModel): ViewModel
}
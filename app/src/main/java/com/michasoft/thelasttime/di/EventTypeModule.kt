package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EventTypeActivity
import com.michasoft.thelasttime.view.EventTypeListActivity
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import com.michasoft.thelasttime.viewModel.EventTypeViewModel
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
    internal abstract fun eventTypeActivity(): EventTypeActivity

    @Binds
    @IntoMap
    @ViewModelKey(EventTypeViewModel::class)
    abstract fun bindViewModel(viewModel: EventTypeViewModel): ViewModel
}
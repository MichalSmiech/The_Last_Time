package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.viewModel.EditEventViewModel
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import com.michasoft.thelasttime.viewModel.EventListViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by mśmiech on 29.04.2022.
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditEventViewModel::class)
    abstract fun bindEditEventViewModel(viewModel: EditEventViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventInstanceViewModel::class)
    abstract fun bindEventInstanceViewModel(instanceViewModel: EventInstanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    abstract fun bindEventListViewModel(viewModel: EventListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindEventViewModel(viewModel: EventViewModel): ViewModel
}
package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EditEventTypeActivity
import com.michasoft.thelasttime.view.EventTypeListActivity
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by mśmiech on 25.05.2021.
 */

@Module
abstract class EditEventTypeModule  {
    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun editEventTypeActivity(): EditEventTypeActivity

    @Binds
    @IntoMap
    @ViewModelKey(EditEventTypeViewModel::class)
    abstract fun bindViewModel(viewModel: EditEventTypeViewModel): ViewModel
}
package com.michasoft.thelasttime.di

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.view.EditEventActivity
import com.michasoft.thelasttime.viewModel.EditEventViewModel
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
    internal abstract fun editEventTypeActivity(): EditEventActivity

    @Binds
    @IntoMap
    @ViewModelKey(EditEventViewModel::class)
    abstract fun bindViewModel(viewModel: EditEventViewModel): ViewModel
}
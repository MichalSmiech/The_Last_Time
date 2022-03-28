package com.michasoft.thelasttime.di

import com.michasoft.thelasttime.MainActivity
import com.michasoft.thelasttime.view.bottomSheet.AddEventInstanceBottomSheet
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by mśmiech on 28.03.2022.
 */
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun addEventInstanceBottomSheet(): AddEventInstanceBottomSheet
}
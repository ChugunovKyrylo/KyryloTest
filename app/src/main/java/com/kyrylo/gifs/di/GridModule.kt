package com.kyrylo.gifs.di

import com.kyrylo.gifs.ui.grid.data.GridStateMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class GridModule {

    @Provides
    @ViewModelScoped
    fun provideGridStateMapper(): GridStateMapper = GridStateMapper()

}
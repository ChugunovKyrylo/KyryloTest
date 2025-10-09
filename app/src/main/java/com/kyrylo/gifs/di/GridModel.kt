package com.kyrylo.gifs.di

import com.kyrylo.gifs.ui.mapper.GifResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class GridModel {

    @Provides
    @ViewModelScoped
    fun provideGifResponseMapper() = GifResponseMapper()

}
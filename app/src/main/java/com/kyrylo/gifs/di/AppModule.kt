package com.kyrylo.gifs.di

import com.kyrylo.gifs.data.remote.ApiKeyInterceptor
import com.kyrylo.gifs.data.remote.RemoteGifApi
import com.kyrylo.gifs.data.repository.GifsRepositoryImpl
import com.kyrylo.gifs.domain.repository.GifsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRemoteGifApi(): RemoteGifApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RemoteGifApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGifsRepository(remoteGifApi: RemoteGifApi): GifsRepository {
        return GifsRepositoryImpl(remoteGifApi)
    }

}
package com.kyrylo.gifs.data.repository

import android.util.Log
import com.kyrylo.gifs.data.remote.RemoteGifApi
import com.kyrylo.gifs.domain.repository.GifsRepository
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteGifApi
): GifsRepository {
    override suspend fun getGifs(query: String) {
        val response = remoteApi.getGifs(query)
        Log.d("GifsRepositoryImpl", "${response.data}")
    }
}
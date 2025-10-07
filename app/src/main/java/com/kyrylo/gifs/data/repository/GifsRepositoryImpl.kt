package com.kyrylo.gifs.data.repository

import com.kyrylo.gifs.data.remote.GiphyApiResponse
import com.kyrylo.gifs.data.remote.RemoteGifApi
import com.kyrylo.gifs.domain.repository.GifsRepository
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteGifApi
) : GifsRepository {

    override suspend fun getGifs(
        query: String,
        pageSize: Int,
        offset: Int
    ): GiphyApiResponse {
        return remoteApi.getGifs(query, pageSize, offset)
    }

}
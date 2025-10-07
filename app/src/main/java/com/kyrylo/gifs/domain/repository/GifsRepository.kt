package com.kyrylo.gifs.domain.repository

import com.kyrylo.gifs.data.remote.GiphyApiResponse

interface GifsRepository {

    suspend fun getGifs(query: String, pageSize: Int, offset: Int): GiphyApiResponse

}
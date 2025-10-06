package com.kyrylo.gifs.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteGifApi {

    @GET("gifs/search")
    suspend fun getGifs(
        @Query("q")
        query: String,
        @Query("limit")
        limit: Int = 25,
        @Query("offset")
        offset: Int = 0,
        @Query("rating")
        r: String = "g",
        @Query("lang")
        lang: String = "en",
        @Query("bundle")
        bundle: String = "messaging_non_clips"
    ): GiphyApiResponse

}
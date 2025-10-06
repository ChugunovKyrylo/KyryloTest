package com.kyrylo.gifs.domain.repository

interface GifsRepository {

    suspend fun getGifs(query: String)

}
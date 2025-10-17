package com.kyrylo.gifs.domain.usecases

import com.kyrylo.gifs.data.remote.GiphyApiResponse
import com.kyrylo.gifs.domain.repository.GifsRepository
import javax.inject.Inject

class GetGifsUseCase @Inject constructor(
    private val gifsRepository: GifsRepository
) {

    suspend operator fun invoke(query: String, pageSize: Int, offset: Int): GiphyApiResponse {
        if(query.isEmpty()) throw IllegalStateException()
        if(pageSize <= 0) throw IllegalStateException()
        if(offset < 0) throw IllegalStateException()
       return gifsRepository.getGifs(query, pageSize, offset)
    }

}
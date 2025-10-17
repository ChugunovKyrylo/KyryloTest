package com.kyrylo.gifs.data

import com.kyrylo.gifs.data.remote.GifResponse
import com.kyrylo.gifs.data.remote.GiphyApiResponse
import com.kyrylo.gifs.data.remote.Images
import com.kyrylo.gifs.data.remote.User
import com.kyrylo.gifs.domain.repository.GifsRepository

class FakeRepository: GifsRepository {

    override suspend fun getGifs(
        query: String,
        pageSize: Int,
        offset: Int
    ): GiphyApiResponse {
        return GiphyApiResponse(
            data = getMockGifResponses(),
            meta = null,
            pagination = null
        )
    }

    private fun getMockGifResponses(): List<GifResponse> {
        return List(20) {
            GifResponse(
                type = null,
                id = null,
                url = null,
                slug = null,
                bitlyUrl = null,
                bitlyGifUrl = null,
                embedUrl = null,
                username = null,
                source = null,
                title = null,
                rating = null,
                contentUrl = null,
                sourceTld = null,
                sourcePostUrl = null,
                sourceCaption = null,
                isSticker = null,
                importDatetime = null,
                trendingDatetime = null,
                images = createFakeImages(),
                user = createFakeUser(),
                analyticsResponsePayload = null,
                analytics = null,
                altText = null,
                isLowContrast = null
            )
        }
    }

    private fun createFakeImages(): Images? {
        return null
    }

    private fun createFakeUser(): User? {
        return null
    }

}
package com.kyrylo.gifs.ui.mapper

import com.kyrylo.gifs.data.remote.GifResponse
import com.kyrylo.gifs.data.remote.User
import com.kyrylo.gifs.ui.models.GifModel
import com.kyrylo.gifs.ui.models.UserModel

class GifResponseMapper {

    fun map(responses: List<GifResponse>?): List<GifModel> {
        return responses?.mapIndexed { index, response -> map(response, index) } ?: emptyList()
    }

    fun map(response: GifResponse?, order: Int = 0): GifModel {
        return GifModel(
            user = mapUser(response?.user),
            title = response?.title ?: "",
            imageUrl = response?.images?.original?.url ?: "",
            description = response?.altText ?: "",
            source = response?.source ?: "",
            order = order,
            id = response?.id ?: ""
        )
    }

    private fun mapUser(user: User?): UserModel {
        return UserModel(
            avatarUrl = user?.avatarUrl ?: "",
            bannerImage = user?.bannerImage ?: "",
            bannerUrl = user?.bannerUrl ?: "",
            profileUrl = user?.profileUrl ?: "",
            username = user?.username ?: "",
            displayName = user?.displayName ?: "",
            description = user?.description ?: "",
            instagramUrl = user?.instagramUrl ?: "",
            websiteUrl = user?.websiteUrl ?: "",
            isVerified = user?.isVerified
        )
    }

}
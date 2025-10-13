package com.kyrylo.gifs.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GiphyApiResponse(
    @SerializedName("data")
    val data: List<GifResponse>?,

    @SerializedName("meta")
    val meta: Meta?,

    @SerializedName("pagination")
    val pagination: Pagination?
)

@Keep
data class Meta(
    @SerializedName("status")
    val status: Int?,

    @SerializedName("msg")
    val msg: String?,

    @SerializedName("response_id")
    val responseId: String?
)

@Keep
data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int?,

    @SerializedName("count")
    val count: Int?,

    @SerializedName("offset")
    val offset: Int?
)

@Keep
data class GifResponse(
    val type: String?,
    val id: String?,
    val url: String?,
    val slug: String?,
    @SerializedName("bitly_gif_url") val bitlyGifUrl: String?,
    @SerializedName("bitly_url") val bitlyUrl: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    val username: String?,
    val source: String?,
    val title: String?,
    val rating: String?,
    @SerializedName("content_url") val contentUrl: String?,
    @SerializedName("source_tld") val sourceTld: String?,
    @SerializedName("source_post_url") val sourcePostUrl: String?,
    @SerializedName("source_caption") val sourceCaption: String?,
    @SerializedName("is_sticker") val isSticker: Int?,
    @SerializedName("import_datetime") val importDatetime: String?,
    @SerializedName("trending_datetime") val trendingDatetime: String?,
    val images: Images?,
    val user: User?,
    @SerializedName("analytics_response_payload") val analyticsResponsePayload: String?,
    val analytics: Analytics?,
    @SerializedName("alt_text") val altText: String?,
    @SerializedName("is_low_contrast") val isLowContrast: Boolean?
)

@Keep
data class Images(
    @SerializedName("original")
    val original: GifImage?,

    @SerializedName("fixed_height")
    val fixedHeight: GifImage?,

    @SerializedName("fixed_height_downsampled")
    val fixedHeightDownsampled: GifImage?,

    @SerializedName("fixed_height_small")
    val fixedHeightSmall: GifImage?,

    @SerializedName("fixed_width")
    val fixedWidth: GifImage?,

    @SerializedName("fixed_width_downsampled")
    val fixedWidthDownsampled: GifImage?,

    @SerializedName("fixed_width_small")
    val fixedWidthSmall: GifImage?
)

@Keep
data class GifImage(
    val height: String?,
    val width: String?,
    val size: String?,
    val url: String?,
    @SerializedName("mp4_size") val mp4Size: String?,
    val mp4: String?,
    @SerializedName("webp_size") val webpSize: String?,
    val webp: String?,
    val frames: String?,
    val hash: String?
)

@Keep
data class Analytics(
    val onload: AnalyticsEvent?,
    val onclick: AnalyticsEvent?,
    val onsent: AnalyticsEvent?
)

@Keep
data class AnalyticsEvent(
    val url: String?
)

@Keep
data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("banner_image")
    val bannerImage: String?,

    @SerializedName("banner_url")
    val bannerUrl: String?,

    @SerializedName("profile_url")
    val profileUrl: String?,

    val username: String?,

    @SerializedName("display_name")
    val displayName: String?,

    val description: String?,

    @SerializedName("instagram_url")
    val instagramUrl: String?,

    @SerializedName("website_url")
    val websiteUrl: String?,

    @SerializedName("is_verified")
    val isVerified: Boolean?
)
package com.kyrylo.gifs.data.remote

import com.google.gson.annotations.SerializedName

data class GiphyApiResponse(
    @SerializedName("data") val data: List<GifResponse>?,
    @SerializedName("meta") val meta: Meta?,
    @SerializedName("pagination") val pagination: Pagination?
)

data class Meta(
    @SerializedName("status") val status: Int?,
    @SerializedName("msg") val msg: String?,
    @SerializedName("response_id") val responseId: String?
)

data class Pagination(
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("count") val count: Int?,
    @SerializedName("offset") val offset: Int?
)

data class GifResponse(
    @SerializedName("type") val type: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("bitly_gif_url") val bitlyGifUrl: String?,
    @SerializedName("bitly_url") val bitlyUrl: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("content_url") val contentUrl: String?,
    @SerializedName("source_tld") val sourceTld: String?,
    @SerializedName("source_post_url") val sourcePostUrl: String?,
    @SerializedName("source_caption") val sourceCaption: String?,
    @SerializedName("is_sticker") val isSticker: Int?,
    @SerializedName("import_datetime") val importDatetime: String?,
    @SerializedName("trending_datetime") val trendingDatetime: String?,
    @SerializedName("images") val images: Images?,
    @SerializedName("user") val user: User?,
    @SerializedName("analytics_response_payload") val analyticsResponsePayload: String?,
    @SerializedName("analytics") val analytics: Analytics?,
    @SerializedName("alt_text") val altText: String?,
    @SerializedName("is_low_contrast") val isLowContrast: Boolean?
)

data class Images(
    @SerializedName("original") val original: GifImage?,
    @SerializedName("fixed_height") val fixedHeight: GifImage?,
    @SerializedName("fixed_height_downsampled") val fixedHeightDownsampled: GifImage?,
    @SerializedName("fixed_height_small") val fixedHeightSmall: GifImage?,
    @SerializedName("fixed_width") val fixedWidth: GifImage?,
    @SerializedName("fixed_width_downsampled") val fixedWidthDownsampled: GifImage?,
    @SerializedName("fixed_width_small") val fixedWidthSmall: GifImage?
)

data class GifImage(
    @SerializedName("height") val height: String?,
    @SerializedName("width") val width: String?,
    @SerializedName("size") val size: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("mp4_size") val mp4Size: String?,
    @SerializedName("mp4") val mp4: String?,
    @SerializedName("webp_size") val webpSize: String?,
    @SerializedName("webp") val webp: String?,
    @SerializedName("frames") val frames: String?,
    @SerializedName("hash") val hash: String?
)

data class Analytics(
    @SerializedName("onload") val onload: AnalyticsEvent?,
    @SerializedName("onclick") val onclick: AnalyticsEvent?,
    @SerializedName("onsent") val onsent: AnalyticsEvent?
)

data class AnalyticsEvent(
    @SerializedName("url") val url: String?
)

data class User(
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("banner_image") val bannerImage: String?,
    @SerializedName("banner_url") val bannerUrl: String?,
    @SerializedName("profile_url") val profileUrl: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("instagram_url") val instagramUrl: String?,
    @SerializedName("website_url") val websiteUrl: String?,
    @SerializedName("is_verified") val isVerified: Boolean?
)
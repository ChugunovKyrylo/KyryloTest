package com.kyrylo.gifs.ui.models

class UserModel(
    val avatarUrl: String,
    val bannerImage: String,
    val bannerUrl: String,
    val profileUrl: String,
    val username: String,
    val displayName: String,
    val description: String,
    val instagramUrl: String,
    val websiteUrl: String,
    val isVerified: Boolean?
) : java.io.Serializable
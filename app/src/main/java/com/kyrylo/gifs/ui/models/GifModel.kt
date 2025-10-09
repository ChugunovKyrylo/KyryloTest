package com.kyrylo.gifs.ui.models

class GifModel(
    val user: UserModel,
    val title: String,
    val imageUrl: String,
    val description: String,
    val source: String,
    val order: Int,
    val id: String
) : java.io.Serializable
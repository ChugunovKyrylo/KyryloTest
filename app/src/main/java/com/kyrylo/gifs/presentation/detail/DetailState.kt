package com.kyrylo.gifs.presentation.detail

import com.kyrylo.gifs.presentation.models.GifModel

class DetailState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val errorMessage: String? = null,
    val gifModel: GifModel? = null
)
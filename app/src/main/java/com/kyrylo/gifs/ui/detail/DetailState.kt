package com.kyrylo.gifs.ui.detail

import com.kyrylo.gifs.ui.models.GifModel

class DetailState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val errorMessage: String? = null,
    val gifModel: GifModel? = null
)
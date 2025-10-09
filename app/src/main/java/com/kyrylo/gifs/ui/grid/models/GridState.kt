package com.kyrylo.gifs.ui.grid.models

import com.kyrylo.gifs.data.remote.GifResponse

data class GridState(
    val query: String = "",
    val paging: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 25,
    val itemsToPaging: Int = 10,
    val gifs: List<GifResponse> = emptyList(),
    val error: Boolean = false,
    val errorMessage: String? = ""
)
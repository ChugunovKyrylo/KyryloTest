package com.kyrylo.gifs.presentation.grid

import com.kyrylo.gifs.presentation.models.GifModel

data class GridState(
    val query: String = "",
    val isProcessPaging: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 25,
    val itemsToPaging: Int = 5,
    val gifs: List<GifModel> = emptyList(),
    val error: Boolean = false,
) {

    fun isEmptyGifs() = gifs.isEmpty() && isProcessPaging.not() && error.not()

}
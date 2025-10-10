package com.kyrylo.gifs.ui.grid

import com.kyrylo.gifs.ui.models.GifModel

data class GridState(
    val query: String = "",
    val isProcessPaging: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 25,
    val itemsToPaging: Int = 5,
    val gifs: List<GifModel> = emptyList(),
    val error: Boolean = false,
    val errorMessage: String? = ""
) {

    fun isEmptyGifs() = gifs.isEmpty() && isProcessPaging.not() && error.not()

}
package com.kyrylo.gifs.ui.grid.models

class GridStateUI(
    val query: String,
    val isProcessPaging: Boolean,
    val itemsToPaging: Int,
    val gifs: List<GifElementUI>,
    val error: Boolean,
    val errorMessage: String?
) {

    fun isEmptyGifs() = gifs.isEmpty()

}
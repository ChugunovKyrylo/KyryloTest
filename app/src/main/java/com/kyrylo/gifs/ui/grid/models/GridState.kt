package com.kyrylo.gifs.ui.grid.models

data class GridState(
    val query: String = "",
    val isProcessPaging: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 25,
    val itemsToPaging: Int = 10,
    val gifs: List<GridGifItemModel> = emptyList(),
    val error: Boolean = false,
    val errorMessage: String? = ""
) {

    fun isEmptyGifs() = gifs.isEmpty()

}
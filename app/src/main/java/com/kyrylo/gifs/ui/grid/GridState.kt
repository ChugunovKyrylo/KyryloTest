package com.kyrylo.gifs.ui.grid

import com.kyrylo.gifs.DataResult
import com.kyrylo.gifs.data.remote.GifResponse

data class GridState(
    val query: String = "",
    val paging: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 25,
    val itemsToPaging: Int = 5,
    val gifs: DataResult<List<GifResponse>> = DataResult.Empty()
)
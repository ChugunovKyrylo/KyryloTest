package com.kyrylo.gifs.ui.grid.data

import com.kyrylo.gifs.data.remote.GifResponse
import com.kyrylo.gifs.ui.grid.models.GridGifItemModel

class GridGifResponsesMapper {

    fun map(responseList: List<GifResponse>): List<GridGifItemModel> {
        return responseList.mapIndexed { index, response ->
            GridGifItemModel(
                order = index,
                id = response.id,
                imageUrl = response.images.original.url
            )
        }
    }

}
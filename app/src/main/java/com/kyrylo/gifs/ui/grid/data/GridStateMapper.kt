package com.kyrylo.gifs.ui.grid.data

import com.kyrylo.gifs.data.remote.GifResponse
import com.kyrylo.gifs.ui.grid.models.GifElementUI
import com.kyrylo.gifs.ui.grid.models.GridState
import com.kyrylo.gifs.ui.grid.models.GridStateUI

class GridStateMapper {

    fun map(state: GridState): GridStateUI {
        return GridStateUI(
            query = state.query,
            isProcessPaging = state.paging,
            itemsToPaging = state.itemsToPaging,
            gifs = mapGifResponses(state.gifs),
            error = state.error,
            errorMessage = state.errorMessage
        )
    }

    private fun mapGifResponses(responseList: List<GifResponse>): List<GifElementUI> {
        return responseList.mapIndexed { index, response ->
            GifElementUI(
                id = index,
                imageUrl = response.images.original.url,
                description = response.altText,
                source = response.source
            )
        }
    }

}
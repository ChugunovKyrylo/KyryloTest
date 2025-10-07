package com.kyrylo.gifs.ui.grid

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kyrylo.gifs.DataResult
import com.kyrylo.gifs.R
import com.kyrylo.gifs.data.remote.GifResponse

@Composable
fun GridScreen() {
    val viewmodel: GridViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    var query: String by remember(state.query) {
        mutableStateOf(state.query)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewmodel.onChangeQuery(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        when (val currentGifs = state.gifs) {
            is DataResult.Empty<List<GifResponse>> -> EmptyCurrentGifsView()
            is DataResult.Error<List<GifResponse>> -> ErrorCurrentGifsView()
            is DataResult.Received<List<GifResponse>> -> ReceivedCurrentGifsView(
                gifs = currentGifs.data,
                itemsToPaging = state.itemsToPaging,
                paging = state.paging,
                onRequestPaging = viewmodel::requestPaging
            )
        }
    }

}

@Composable
private fun ReceivedCurrentGifsView(
    gifs: List<GifResponse>,
    itemsToPaging: Int,
    paging: Boolean,
    onRequestPaging: () -> Unit
) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items = gifs) { index, item ->
            if (index == gifs.lastIndex - itemsToPaging) {
                onRequestPaging()
            }
            val loader = ImageLoader.Builder(context)
                .components {
                    if (SDK_INT >= 28) {
                        add(AnimatedImageDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build()
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.images.original.url)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.error_gif_loading),
                placeholder = painterResource(R.drawable.placeholder_gif_loading),
                imageLoader = loader,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
        if (paging) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(200.dp)
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ErrorCurrentGifsView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Unexpected Error",
            fontSize = 30.sp,
            color = Color.Red
        )
    }
}

@Composable
private fun EmptyCurrentGifsView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "There are no any searched gif",
            fontSize = 30.sp,
            color = Color.Black
        )
    }
}
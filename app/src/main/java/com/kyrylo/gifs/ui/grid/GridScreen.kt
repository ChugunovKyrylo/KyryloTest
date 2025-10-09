package com.kyrylo.gifs.ui.grid

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.clickable
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
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kyrylo.gifs.R
import com.kyrylo.gifs.ui.models.GifModel

@Composable
fun GridScreen(onGifClicked: (GifModel) -> Unit) {
    val viewmodel: GridViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    when (val currentState = state) {
        else -> {
            GridStateScreen(
                state = currentState,
                onChangeQuery = viewmodel::onChangeQuery,
                onRequestPaging = viewmodel::requestPaging,
                onGifClicked = onGifClicked
            )
        }
    }
}

@Composable
private fun GridStateScreen(
    state: GridState,
    onChangeQuery: (String) -> Unit,
    onRequestPaging: () -> Unit,
    onGifClicked: (GifModel) -> Unit
) {
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
                onChangeQuery(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        when {
            state.isEmptyGifs() -> EmptyGifsView()
            else -> GifListView(
                state = state,
                onRequestPaging = onRequestPaging,
                onGifClicked = onGifClicked
            )
        }
    }
}

@Composable
private fun GifListView(
    state: GridState,
    onRequestPaging: () -> Unit,
    onGifClicked: (GifModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items = state.gifs, key = { index, gif -> gif.order }) { index, item ->
            if (index >= state.gifs.lastIndex - state.itemsToPaging) {
                onRequestPaging()
            }
            GifItemView(item, onGifClicked)
        }
        if (state.isProcessPaging) {
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
private fun GifItemView(item: GifModel, onGifClicked: (GifModel) -> Unit) {
    val imageUrl by remember {
        mutableStateOf(item.imageUrl)
    }
    val context = LocalContext.current
    val loader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
    }
    val request = remember {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
    AsyncImage(
        model = request,
        error = painterResource(R.drawable.error_gif_loading),
        placeholder = painterResource(R.drawable.placeholder_gif_loading),
        imageLoader = loader,
        contentDescription = null,
        modifier = Modifier
            .size(200.dp)
            .clickable { onGifClicked(item) }
    )
}

@Composable
private fun EmptyGifsView() {
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
package com.kyrylo.gifs.ui.grid

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    val isEmptyGifs by remember(state.isEmptyGifs()) {
        derivedStateOf { state.isEmptyGifs() }
    }
    val animatedGridWeight by animateFloatAsState(
        if (isEmptyGifs) 0f else 1f,
        animationSpec = tween(300, easing = LinearEasing)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isEmptyGifs,
            enter = fadeIn(tween(300, easing = LinearEasing)),
            exit = fadeOut(tween(300, easing = LinearEasing))
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "Enter a searching key",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
            }
        }
        GridTextField(
            q = state.query,
            onChangeQuery = viewmodel::onChangeQuery
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedGridWeight)
        ) {
            Spacer(Modifier.height(10.dp))
            GifListView(
                state = state,
                onRequestPaging = viewmodel::requestPaging,
                onGifClicked = onGifClicked,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun GridTextField(q: String, onChangeQuery: (String) -> Unit) {
    var query: String by remember(q) {
        mutableStateOf(q)
    }
    OutlinedTextField(
        value = query,
        placeholder = {
            Text(text = "smile")
        },
        onValueChange = {
            query = it
            onChangeQuery(it)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
    )
}

@Composable
private fun GifListView(
    state: GridState,
    onRequestPaging: () -> Unit,
    onGifClicked: (GifModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(100.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        itemsIndexed(items = state.gifs, key = { index, gif -> gif.getKey() }) { index, item ->
            if (index >= state.gifs.lastIndex - state.itemsToPaging) {
                onRequestPaging()
            }
            GifItemView(
                item = item,
                modifier = Modifier.clickable { onGifClicked(item) }
            )
        }
        if (state.isProcessPaging) {
            item(span = { GridItemSpan(2) }) {
                AnimatedLoadingItem()
            }
        }
    }
}

@Composable
fun GifItemView(item: GifModel, modifier: Modifier = Modifier) {
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
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }
    AsyncImage(
        model = request,
        error = painterResource(R.drawable.error_gif_loading),
        placeholder = painterResource(R.drawable.placeholder_gif_loading),
        imageLoader = loader,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(100.dp)
    )
}

@Composable
private fun AnimatedLoadingItem() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}
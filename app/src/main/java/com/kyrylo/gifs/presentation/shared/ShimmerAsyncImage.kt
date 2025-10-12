package com.kyrylo.gifs.presentation.shared

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ShimmerAsyncImage(url: String, modifier: Modifier = Modifier) {
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
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }
    var isActiveShimmer by remember {
        mutableStateOf(true)
    }
    AsyncImage(
        model = request,
        imageLoader = loader,
        contentDescription = null,
        onState = { asyncImageState ->
            isActiveShimmer = asyncImageState !is AsyncImagePainter.State.Success
        },
        contentScale = ContentScale.Crop,
        modifier = modifier.size(100.dp)
            .shimmerEffect(isActiveShimmer)
    )
}
package com.kyrylo.gifs.presentation.grid

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kyrylo.gifs.MainActivity
import com.kyrylo.gifs.R
import com.kyrylo.gifs.presentation.models.GifModel
import com.kyrylo.gifs.presentation.shared.ShimmerAsyncImage

@Composable
fun GridScreen(
    onGifClicked: (GifModel) -> Unit,
    onShowErrorPaging: () -> Unit,
    retryLoadingGridPage: Boolean
) {

    val activity = LocalContext.current as MainActivity

    val viewmodel: GridViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()
    val isEmptyGifs by remember(state.isEmptyGifs()) {
        derivedStateOf { state.isEmptyGifs() }
    }
    val animatedGridWeight by animateFloatAsState(
        if (isEmptyGifs) 0f else 1f,
        animationSpec = tween(300, easing = LinearEasing)
    )

    BackHandler { viewmodel.handleBackPressed() }

    LaunchedEffect(retryLoadingGridPage) {
        if (retryLoadingGridPage) {
            Log.d("MainActivity", "retry paging")
            viewmodel.onRetryPaging()
        }
    }

    LaunchedEffect(0) {
        viewmodel.gridAction.collect { action ->
            when (action) {
                GridAction.CloseApp -> activity.finish()
                GridAction.SendError -> onShowErrorPaging()
            }
        }
    }

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
                    text = stringResource(R.string.enter_a_searching_key),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
            }
        }
        SearchTextField(
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
private fun SearchTextField(q: String, onChangeQuery: (String) -> Unit) {
    var query: String by remember(q) {
        mutableStateOf(q)
    }
    OutlinedTextField(
        value = query,
        placeholder = {
            Text(text = stringResource(R.string.smile))
        },
        onValueChange = {
            query = it
            onChangeQuery(it)
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)
    )
}

@Composable
private fun GifListView(
    state: GridState,
    onRequestPaging: () -> Unit,
    onGifClicked: (GifModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val showLoading by remember(state.isProcessPaging, state.error) {
        derivedStateOf {
            state.isProcessPaging && state.error.not()
        }
    }
    val lazyGridState = rememberLazyGridState()
    val isSendRequest by remember(state.itemsToPaging, state.overflow) {
        derivedStateOf {
            val currentScrollPosition =
                lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val allItemsInListCount = lazyGridState.layoutInfo.totalItemsCount
            val lastItemsListIndex = allItemsInListCount - 1
            allItemsInListCount > 0 && currentScrollPosition >= lastItemsListIndex - state.itemsToPaging && state.overflow.not()
        }
    }
    LaunchedEffect(isSendRequest) {
        if (isSendRequest) {
            Log.d("GridScreen", "onRequestPaging")
            onRequestPaging()
        }
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.FixedSize(100.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(items = state.gifs, key = { gif -> gif.getKey() }) { item ->
            GifItemView(
                item = item,
                modifier = Modifier.clickable { onGifClicked(item) }
            )
        }
        if (showLoading) {
            item {
                AnimatedLoadingItem()
            }
        }
    }
}

@Composable
fun GifItemView(item: GifModel, modifier: Modifier = Modifier) {
    val imageUrl by remember(item.imageUrl) {
        mutableStateOf(item.imageUrl)
    }
    ShimmerAsyncImage(
        url = imageUrl,
        modifier = modifier
    )
}

@Composable
private fun AnimatedLoadingItem() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        CircularProgressIndicator()
    }
}
package com.kyrylo.gifs.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kyrylo.gifs.ui.models.GifModel

@Composable
fun DetailScreen(model: GifModel?, onBack: () -> Unit) {
    var enableErrorScreen by remember {
        mutableStateOf(true)
    }
    BackHandler {
        enableErrorScreen = false
        onBack()
    }

    when (model) {
        null -> if (enableErrorScreen) ErrorDetailScreen()
        else -> DetailStateScreen(model)
    }
}

@Composable
private fun DetailStateScreen(model: GifModel) {

}

@Composable
private fun ErrorDetailScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Unexpected error",
            fontSize = 30.sp,
            color = Color.Red
        )
    }
}
package com.kyrylo.gifs.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kyrylo.gifs.ui.grid.GifItemView
import com.kyrylo.gifs.ui.models.GifModel
import com.kyrylo.gifs.ui.models.UserModel

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
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ListBlock {
            DetailedGifView(item = model)
        }
        ListBlock(horizontalAlignment = Alignment.Start) {
            DescriptionGifView(description = model.description)
        }
        ListBlock(horizontalAlignment = Alignment.Start) {
            SourceGifView(source = model.source)
        }
        ListBlock(horizontalAlignment = Alignment.Start) {
            IdentityGifView(id = model.id)
        }
        ListBlock {
            UserView(userModel = model.user)
        }
    }
}

@Composable
private fun ColumnScope.UserView(userModel: UserModel) {
    val actualProfileUrl by remember {
        derivedStateOf {
            userModel.profileUrl.ifEmpty { "Unknown" }
        }
    }
    val actualDisplayedName by remember {
        derivedStateOf {
            userModel.displayName.ifEmpty { "Unknown" }
        }
    }

    val actualDescription by remember {
        derivedStateOf {
            userModel.description.ifEmpty { "Unknown" }
        }
    }

    AsyncImage(
        model = userModel.avatarUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
    )
    Text(
        text = actualDisplayedName,
        fontSize = 24.sp,
        color = Color.Black
    )

    Text(
        text = "Profile Url:",
        fontSize = 24.sp,
        color = Color.Black,
        modifier = Modifier.align(Alignment.Start)
    )
    Text(
        text = actualProfileUrl,
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier.align(Alignment.Start)
    )

    Text(
        text = "Description:",
        fontSize = 24.sp,
        color = Color.Black,
        modifier = Modifier.align(Alignment.Start)
    )

    Text(
        text = actualDescription,
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier.align(Alignment.Start)
    )

}

@Composable
private fun IdentityGifView(id: String) {
    val actualItemIdentity by remember {
        derivedStateOf {
            id.ifEmpty { "Unknown" }
        }
    }
    Text(
        text = "ID:",
        fontSize = 24.sp,
        color = Color.Black,
    )
    Text(
        text = actualItemIdentity,
        fontSize = 20.sp,
        color = Color.Black
    )
}

@Composable
private fun SourceGifView(source: String) {
    val actualItemSource by remember {
        derivedStateOf {
            source.ifEmpty { "Unknown" }
        }
    }
    Text(
        text = "Source:",
        fontSize = 24.sp,
        color = Color.Black,
    )
    Text(
        text = actualItemSource,
        fontSize = 20.sp,
        color = Color.Black
    )
}

@Composable
private fun DescriptionGifView(description: String) {
    val actualItemDescription by remember {
        derivedStateOf {
            description.ifEmpty { "Gif image" }
        }
    }
    Text(
        text = "Description:",
        fontSize = 24.sp,
        color = Color.Black,
    )
    Text(
        text = actualItemDescription,
        fontSize = 20.sp,
        color = Color.Black
    )
}

@Composable
private fun DetailedGifView(
    item: GifModel
) {
    val actualItemTitle by remember {
        derivedStateOf {
            item.title.ifEmpty { "Unknown" }
        }
    }
    GifItemView(item)
    Text(
        text = actualItemTitle,
        fontSize = 24.sp,
        color = Color.Black
    )
}

@Composable
private fun ListBlock(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(4.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        content()
    }
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
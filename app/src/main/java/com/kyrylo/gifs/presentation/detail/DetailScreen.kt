package com.kyrylo.gifs.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kyrylo.gifs.R
import com.kyrylo.gifs.presentation.grid.GifItemView
import com.kyrylo.gifs.presentation.models.GifModel
import com.kyrylo.gifs.presentation.models.UserModel
import com.kyrylo.gifs.presentation.ui.theme.AppTypography
import com.kyrylo.gifs.presentation.ui.theme.errorLight
import com.kyrylo.gifs.presentation.ui.theme.onErrorLight

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
        null -> if (enableErrorScreen) ErrorDetailScreen(onBack)
        else -> DetailStateScreen(
            model = model, onBack = {
                enableErrorScreen = false
                onBack()
            }
        )
    }
}

@Composable
private fun DetailStateScreen(model: GifModel, onBack: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ListHeader {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterStart)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onBack
                        )
                )
                Text(
                    text = stringResource(R.string.gif_header_caps),
                    style = AppTypography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
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
            ListHeader {
                Text(
                    text = stringResource(R.string.user_header_caps),
                    style = AppTypography.titleMedium
                )
            }
            ListBlock {
                UserView(userModel = model.user)
            }
        }
    }
}

@Composable
private fun ColumnScope.UserView(userModel: UserModel) {
    val context = LocalContext.current
    val actualProfileUrl by remember {
        derivedStateOf {
            userModel.profileUrl.ifEmpty { context.getString(R.string.unknown) }
        }
    }
    val actualDisplayedName by remember {
        derivedStateOf {
            userModel.displayName.ifEmpty { context.getString(R.string.unknown) }
        }
    }

    val actualDescription by remember {
        derivedStateOf {
            userModel.description.ifEmpty { context.getString(R.string.unknown) }
        }
    }

    AsyncImage(
        model = userModel.avatarUrl,
        contentDescription = null,
        error = painterResource(R.drawable.account_image),
        placeholder = painterResource(R.drawable.account_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
    )
    Text(
        text = actualDisplayedName,
        style = AppTypography.bodyLarge
    )

    Text(
        text = stringResource(R.string.profile_url_colon),
        style = AppTypography.bodyLarge,
        modifier = Modifier.align(Alignment.Start)
    )
    Text(
        text = actualProfileUrl,
        style = AppTypography.bodyMedium,
        modifier = Modifier.align(Alignment.Start)
    )

    Text(
        text = stringResource(R.string.description_colon),
        style = AppTypography.bodyLarge,
        modifier = Modifier.align(Alignment.Start)
    )

    Text(
        text = actualDescription,
        style = AppTypography.bodyMedium,
        modifier = Modifier.align(Alignment.Start)
    )

}

@Composable
private fun IdentityGifView(id: String) {
    val context = LocalContext.current
    val actualItemIdentity by remember {
        derivedStateOf {
            id.ifEmpty { context.getString(R.string.unknown) }
        }
    }
    Text(
        text = stringResource(R.string.id_colon),
        style = AppTypography.bodyLarge
    )
    Text(
        text = actualItemIdentity,
        style = AppTypography.bodyMedium
    )
}

@Composable
private fun SourceGifView(source: String) {
    val context = LocalContext.current
    val actualItemSource by remember {
        derivedStateOf {
            source.ifEmpty { context.getString(R.string.unknown) }
        }
    }
    Text(
        text = stringResource(R.string.source_colon),
        style = AppTypography.bodyLarge
    )
    Text(
        text = actualItemSource,
        style = AppTypography.bodyMedium
    )
}

@Composable
private fun DescriptionGifView(description: String) {
    val context = LocalContext.current
    val actualItemDescription by remember {
        derivedStateOf {
            description.ifEmpty { context.getString(R.string.gif_image) }
        }
    }
    Text(
        text = stringResource(R.string.description_colon),
        style = AppTypography.bodyLarge
    )
    Text(
        text = actualItemDescription,
        style = AppTypography.bodyMedium
    )
}

@Composable
private fun DetailedGifView(
    item: GifModel
) {
    val context = LocalContext.current
    val actualItemTitle by remember {
        derivedStateOf {
            item.title.ifEmpty { context.getString(R.string.unknown) }
        }
    }
    GifItemView(item, modifier = Modifier.size(200.dp))
    Text(
        text = actualItemTitle,
        style = AppTypography.bodyLarge
    )
}

@Composable
private fun ListHeader(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        content()
        Spacer(Modifier.height(16.dp))
    }
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
private fun ErrorDetailScreen(
    onBack: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.unexpected_error),
            fontSize = 30.sp,
            color = errorLight,
            style = AppTypography.titleLarge
        )
        Spacer(Modifier.height(26.dp))
        TextButton(
            onClick = onBack,
            colors = ButtonColors(
                containerColor = errorLight,
                contentColor = onErrorLight,
                disabledContainerColor = errorLight,
                disabledContentColor = onErrorLight
            ),
            modifier = Modifier.width(200.dp)
        ) {
            Text(
                text = stringResource(R.string.back),
                style = AppTypography.bodyLarge
            )
        }
    }
}
package com.kyrylo.gifs.presentation.detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kyrylo.gifs.R
import com.kyrylo.gifs.presentation.grid.GifItemView
import com.kyrylo.gifs.presentation.models.GifModel
import com.kyrylo.gifs.presentation.models.UserModel

@Composable
fun DetailScreen(model: GifModel, onBack: () -> Unit) {
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
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            ),
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
                    style = MaterialTheme.typography.titleMedium
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
    val actualProfileUrl = remember(userModel.profileUrl) {
        userModel.profileUrl.ifEmpty { context.getString(R.string.unknown) }
    }
    val actualDisplayedName = remember(userModel.displayName) {
        userModel.displayName.ifEmpty { context.getString(R.string.unknown) }
    }
    val actualDescription = remember(userModel.description) {
        userModel.description.ifEmpty { context.getString(R.string.unknown) }
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
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary
    )

    Text(
        text = stringResource(R.string.profile_url_colon),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.align(Alignment.Start),
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = actualProfileUrl,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.align(Alignment.Start),
        color = MaterialTheme.colorScheme.tertiary
    )

    Text(
        text = stringResource(R.string.description_colon),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.align(Alignment.Start),
        color = MaterialTheme.colorScheme.secondary
    )

    Text(
        text = actualDescription,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.align(Alignment.Start),
        color = MaterialTheme.colorScheme.tertiary
    )

}

@Composable
private fun IdentityGifView(id: String) {
    val context = LocalContext.current
    val actualItemIdentity = remember(id) {
        id.ifEmpty { context.getString(R.string.unknown) }
    }
    Text(
        text = stringResource(R.string.id_colon),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = actualItemIdentity,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun SourceGifView(source: String) {
    val context = LocalContext.current
    val actualItemSource = remember(source) {
        source.ifEmpty { context.getString(R.string.unknown) }
    }
    Text(
        text = stringResource(R.string.source_colon),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = actualItemSource,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun DescriptionGifView(description: String) {
    val context = LocalContext.current
    val actualItemDescription = remember(description) {
        description.ifEmpty { context.getString(R.string.gif_image) }
    }
    Text(
        text = stringResource(R.string.description_colon),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = actualItemDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun DetailedGifView(
    item: GifModel
) {
    val context = LocalContext.current
    val actualItemTitle = remember(item.title) {
        item.title.ifEmpty { context.getString(R.string.unknown) }
    }
    GifItemView(item, modifier = Modifier.size(200.dp))
    Text(
        text = actualItemTitle,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary
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
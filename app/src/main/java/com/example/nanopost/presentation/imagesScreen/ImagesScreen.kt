package com.example.nanopost.presentation.imagesScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.example.nanopost.R
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.loadState

@Composable
fun ImagesScreen(
    imagesViewModel: ImagesViewModel,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val images = imagesViewModel.images.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.images_big),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = paddingValues,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(
                count = images.itemCount,
                key = images.itemKey { it.id }
            ) { index ->
                val item = images[index]
                if (item != null) {
                    AsyncImage(
                        model = item.sizes.first().url,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        placeholder = painterResource(R.drawable.no_photo),
                        modifier = Modifier
                            .height(125.dp)
                            .clickable(onClick = { onImageClick(item.id) })
                    )
                }
            }

            loadState(images.loadState.append, images::retry)
        }

    }

}
package com.example.nanopost.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nanopost.R
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.presentation.theme.LocalExtendedColors

@Composable
fun PhotoPager(images: List<Image>, modifier: Modifier = Modifier) {
    val maxPage = images.size
    val pagerState = rememberPagerState(pageCount = { maxPage })
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        PhotoPaged(images.elementAtOrNull(page), page + 1, maxPage)
    }
}

@Composable
fun PhotoPaged(image: Image?, page: Int, maxPage: Int) {
    if (image != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = image.sizes.first().url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(R.drawable.no_photo),
                modifier = Modifier.fillMaxWidth()
            )

            if (maxPage != 1) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LocalExtendedColors.current.surface1)
                        .padding(8.dp)
                ) {
                    Text(
                        "$page/$maxPage",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
package com.example.nanopost.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
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
fun PhotoPager(images: List<Image>, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val maxPage = images.size
    val pagerState = rememberPagerState(pageCount = { maxPage })

    val ratio = images.findHighestRatio()
    HorizontalPager(
        state = pagerState,
        modifier = modifier.aspectRatio(ratio)
    ) { page ->
        PhotoPaged(images.elementAtOrNull(page), page + 1, maxPage, onClick)
    }
}

@Composable
fun PhotoPaged(image: Image?, page: Int, maxPage: Int, onClick: (String) -> Unit) {
    if (image != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { onClick(image.id) })
        ) {
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

private fun List<Image>.findHighestRatio(): Float {
    val image = this.maxWith { i1, i2 -> i1.sizes.first().height - i2.sizes.first().height }
    val height = image.sizes.first().height
    val width = image.sizes.first().width.toFloat()
    return width / height
}
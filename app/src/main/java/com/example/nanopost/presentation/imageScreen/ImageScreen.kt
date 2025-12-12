package com.example.nanopost.presentation.imageScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nanopost.R
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.exceptions.AppException
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.ErrorState
import com.example.nanopost.presentation.component.Loading
import com.example.nanopost.presentation.component.UserPostInfo
import com.example.nanopost.presentation.extentions.toAppException
import com.example.nanopost.presentation.postScreen.Error
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImageScreen(
    imageViewModel: ImageViewModel,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
) {
    val screenState = imageViewModel.screenState.collectAsState()

    when (val currentState = screenState.value) {
        is ImageScreenState.Content -> Screen(currentState.image, imageViewModel, onBackClick)
        is ImageScreenState.Error -> Error(currentState.e, imageViewModel::getImage, onLogout)
        ImageScreenState.Loading -> Loading()
    }

}

@Composable
fun Error(ex: AppException, onRetryClick: () -> Unit, onLogout: () -> Unit) {
    if (ex is AuthenticationException) {
        onLogout()
    } else {
        ErrorState(onRetryClick)
    }
}

@Composable
fun Screen(image: Image, imageViewModel: ImageViewModel, onBackClick: () -> Unit) {
    var isShowMenu by remember { mutableStateOf(false) }
    var isUserImage by remember { mutableStateOf(false) }

    LaunchedEffect(image.owner.id) {
        isUserImage = imageViewModel.getUserId() == image.owner.id
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (isUserImage) {
                        IconButton(onClick = { isShowMenu = !isShowMenu }) {
                            Icon(
                                painter = painterResource(R.drawable.more_vert),
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = isShowMenu,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            onDismissRequest = { isShowMenu = false },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.delete),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    imageViewModel.deleteImage()
                                    onBackClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = image.sizes.first().url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(R.drawable.no_photo),
                modifier = Modifier
                    .fillMaxWidth()
                    .zoomable(rememberZoomState())
                    .weight(1f)
            )

            UserPostInfo(
                owner = image.owner,
                dateCreated = image.dateCreated,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
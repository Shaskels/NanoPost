package com.example.nanopost.presentation.newPostScreen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.nanopost.R
import com.example.nanopost.presentation.component.AddImageButton
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.ImageWithDelete
import com.example.nanopost.presentation.theme.LocalExtendedColors
import timber.log.Timber

@Composable
fun NewPostScreen(onClose: () -> Unit, newPostViewModel: NewPostViewModel = hiltViewModel()) {
    val screenState by newPostViewModel.screenState.collectAsState()
    val context = LocalContext.current
    val ableToAddNewImages = NewPostViewModel.MAX_IMAGES_COUNT - screenState.postImages.size != 0

    val pickMultipleMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(NewPostViewModel.MAX_IMAGES_COUNT - screenState.postImages.size + 1)
    ) { uris ->
        if (uris.isNotEmpty()) {
            newPostViewModel.onAddPostImages(uris.map { it.toString() })
            uris.forEach { uri ->
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            Timber.tag("PhotoPicker").d("Number of items selected: ${uris.size}")
        } else {
            Timber.tag("PhotoPicker").d("No media selected")
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.new_post),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        newPostViewModel.onUploadPost()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.check),
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PostTextField(
                value = screenState.postText,
                onValueChanged = { newPostViewModel.onPostTextChange(it) },
                hint = stringResource(R.string.enter_post_text),
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            if (ableToAddNewImages) {
                AddImageButton(
                    onClick = {
                        pickMultipleMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    label = stringResource(R.string.add_image),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            LazyRow(
                userScrollEnabled = true,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items = screenState.postImages) { item ->
                    ImageWithDelete(
                        uri = item,
                        onDeleteClick = { newPostViewModel.onDeletePostImage(item) },
                        onClick = { },
                        modifier = Modifier.height(300.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun PostTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    TextField(
        value = value,
        onValueChanged,
        placeholder = { Text(text = hint, style = textStyle) },
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = LocalExtendedColors.current.surface1,
            ),
        ),
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}
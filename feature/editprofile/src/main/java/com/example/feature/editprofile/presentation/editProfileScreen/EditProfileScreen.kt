package com.example.feature.editprofile.presentation.editProfileScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.component.uicomponents.CustomTextField
import com.example.component.uicomponents.CustomTopBar
import com.example.component.uicomponents.LocalSnackbarHost
import com.example.component.uicomponents.NoPhotoAvatar
import com.example.component.uicomponents.PhotoAvatar
import com.example.component.uicomponents.showSnackbar
import com.example.feature.editprofile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val screenState by editProfileViewModel.screenState.collectAsState()
    val snackbarHost = LocalSnackbarHost.current
    val pickMultipleMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uris ->
        editProfileViewModel.onAvatarChange(uris.toString())
    }

    LaunchedEffect(Unit) {
        editProfileViewModel.startUp()
    }

    LaunchedEffect(screenState.updateResult) {
        when (screenState.updateResult) {
            UpdateResult.Failure ->
                snackbarHost.showSnackbar(
                    message = "Something went wrong",
                    actionLabel = null,
                    onActionPerformed = {},
                    onDismiss = {}
                )

            UpdateResult.Success -> {
                onCloseClick()
                editProfileViewModel.onClose()
            }

            UpdateResult.Loading -> snackbarHost.showSnackbar(
                message = "Profile updating, please wait",
                actionLabel = null,
                onActionPerformed = {},
                onDismiss = {}
            )

            null -> {}

        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.profile),
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(painter = painterResource(R.drawable.close), contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = editProfileViewModel::updateProfile) {
                        Icon(painter = painterResource(R.drawable.check), contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                val avatar =
                    if (screenState.avatarUri != null) screenState.avatarUri else screenState.avatarUrl
                if (avatar != null) {
                    PhotoAvatar(url = avatar, modifier = Modifier.size(60.dp))
                } else {
                    NoPhotoAvatar(screenState.displayName ?: "", modifier = Modifier.size(60.dp))
                }

                IconButton(onClick = {
                    pickMultipleMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }, modifier = Modifier.align(Alignment.Bottom)) {
                    Icon(
                        painter = painterResource(R.drawable.edit_24dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }

            CustomTextField(
                value = screenState.displayName ?: "",
                onValueChange = editProfileViewModel::onDisplayNameChange,
                enabled = true,
                label = stringResource(R.string.display_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            CustomTextField(
                value = screenState.bio ?: "",
                onValueChange = editProfileViewModel::onBioChange,
                enabled = true,
                label = stringResource(R.string.bio),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
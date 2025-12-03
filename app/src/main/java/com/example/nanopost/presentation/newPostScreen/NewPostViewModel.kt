package com.example.nanopost.presentation.newPostScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.example.nanopost.presentation.newPostScreen.newPostScreenState.NewPostScreenState
import com.example.nanopost.presentation.newPostScreen.newPostScreenState.UploadState
import com.example.nanopost.presentation.worker.PostSendWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    companion object {
        const val MAX_IMAGES_COUNT = 4
    }

    private val _screenState = MutableStateFlow(
        NewPostScreenState(
            "", emptyList(),
            UploadState.None
        )
    )
    val screenState: StateFlow<NewPostScreenState> = _screenState.asStateFlow()

    fun onPostTextChange(postText: String) {
        _screenState.update { currentState ->
            currentState.copy(
                postText = postText
            )
        }
    }

    fun onAddPostImages(images: List<Uri>) {
        val allImages = _screenState.value.postImages + images
        _screenState.update { currentState ->
            currentState.copy(
                postImages = allImages
            )
        }
    }

    fun onDeletePostImage(image: Uri) {
        val allImages = _screenState.value.postImages - image
        _screenState.update { currentState ->
            currentState.copy(
                postImages = allImages
            )
        }
    }

    fun onUploadPost() {
        workManager.enqueue(PostSendWorker.newRequest(_screenState.value.postText, _screenState.value.postImages))
    }

}
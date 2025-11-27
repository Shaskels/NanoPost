package com.example.nanopost.presentation.newPostScreen

import androidx.lifecycle.ViewModel
import com.example.nanopost.presentation.newPostScreen.newPostScreenState.NewPostScreenState
import com.example.nanopost.presentation.newPostScreen.newPostScreenState.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor() : ViewModel() {

    companion object{
        const val MAX_IMAGES_COUNT = 5
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

    fun onAddPostImages(images: List<String>) {
        val allImages = _screenState.value.postImages + images
        _screenState.update { currentState ->
            currentState.copy(
                postImages = allImages
            )
        }
    }

    fun onDeletePostImage(image: String) {
        val allImages = _screenState.value.postImages - image
        _screenState.update { currentState ->
            currentState.copy(
                postImages = allImages
            )
        }
    }

    fun onUploadPost(){

    }

}
package com.example.nanopost.presentation.profileScreen

import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.entity.Profile

sealed interface ProfileScreenState {

    data object Loading: ProfileScreenState

    data object Error: ProfileScreenState

    data class Content(val profile: Profile, val images: List<Image>): ProfileScreenState
}
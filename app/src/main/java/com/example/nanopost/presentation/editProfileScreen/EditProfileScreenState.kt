package com.example.nanopost.presentation.editProfileScreen

data class EditProfileScreenState (
    val displayName: String?,
    val bio: String?,
    val avatarUrl: String?,
    val avatarUri: String?,
    val updateResult: UpdateResult?
)

sealed interface UpdateResult{
    data object Success: UpdateResult

    data object Loading: UpdateResult
    data object Failure: UpdateResult
}
package com.example.feature.editprofile.presentation.domain

import android.net.Uri
import com.example.shared.domain.repository.ImagesRepository
import com.example.shared.domain.repository.ProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val imagesRepository: ImagesRepository
) {

    suspend operator fun invoke(displayName: String?, bio: String?, avatar: Uri?) {
        withContext(Dispatchers.Default) {
            profileRepository.updateProfile(
                displayName,
                bio,
                if (avatar == null) null else imagesRepository.uriToImageInfo(avatar)
            )
        }
    }
}
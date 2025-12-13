package com.example.nanopost.domain.usecase

import com.example.shared.domain.repository.ProfileRepository
import javax.inject.Inject

class SubscribeUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(profileId: String){
        profileRepository.subscribeOn(profileId)
    }
}
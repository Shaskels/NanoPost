package com.example.feature.subscribers.presentation.domain

import androidx.paging.PagingData
import com.example.shared.domain.entity.ProfileCompact
import com.example.shared.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileSubscribersUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(profileId: String): Flow<PagingData<ProfileCompact>> {
        return profileRepository.getProfileSubscribers(profileId)
    }
}
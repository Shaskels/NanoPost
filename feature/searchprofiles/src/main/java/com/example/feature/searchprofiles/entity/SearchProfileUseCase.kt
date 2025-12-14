package com.example.feature.searchprofiles.entity

import androidx.paging.PagingData
import com.example.shared.domain.entity.ProfileCompact
import com.example.shared.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(query: String): Flow<PagingData<ProfileCompact>> {
        return profileRepository.searchProfile(query)
    }
}
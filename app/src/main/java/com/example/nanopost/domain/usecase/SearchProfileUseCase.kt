package com.example.nanopost.domain.usecase

import androidx.paging.PagingData
import com.example.nanopost.domain.entity.ProfileCompact
import com.example.nanopost.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(query: String): Flow<PagingData<ProfileCompact>> {
        return profileRepository.searchProfile(query)
    }
}
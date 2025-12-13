package com.example.nanopost.di

import com.example.shared.domain.repository.AuthRepository
import com.example.shared.domain.repository.ImagesRepository
import com.example.shared.domain.repository.PostRepository
import com.example.shared.domain.repository.ProfileRepository
import com.example.shared.remote.AuthRepositoryImpl
import com.example.shared.remote.ImagesRepositoryImpl
import com.example.shared.remote.PostRepositoryImpl
import com.example.shared.remote.ProfileRepositoryImpl
import com.example.shared.settings.data.SettingsRepositoryImpl
import com.example.shared.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindImagesRepository(imagesRepositoryImpl: ImagesRepositoryImpl): ImagesRepository
}
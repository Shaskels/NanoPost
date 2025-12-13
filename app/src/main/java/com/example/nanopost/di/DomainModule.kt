package com.example.nanopost.di

import com.example.nanopost.data.repository.ImagesRepositoryImpl
import com.example.nanopost.data.repository.PostRepositoryImpl
import com.example.nanopost.data.repository.ProfileRepositoryImpl
import com.example.nanopost.domain.repository.ImagesRepository
import com.example.nanopost.domain.repository.PostRepository
import com.example.nanopost.domain.repository.ProfileRepository
import com.example.shared.domain.repository.AuthRepository
import com.example.shared.remote.AuthRepositoryImpl
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
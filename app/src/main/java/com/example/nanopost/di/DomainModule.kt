package com.example.nanopost.di

import com.example.nanopost.data.repository.AuthRepositoryImpl
import com.example.nanopost.data.repository.PostRepositoryImpl
import com.example.nanopost.data.repository.SettingsRepositoryImpl
import com.example.nanopost.domain.repository.AuthRepository
import com.example.nanopost.domain.repository.PostRepository
import com.example.nanopost.domain.repository.SettingsRepository
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
}
package com.example.nanopost.di

import android.content.ContentResolver
import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideWorkManager(@ApplicationContext applicationContext: Context): WorkManager {
        return WorkManager.getInstance(applicationContext)
    }

    @Provides
    fun provideContentResolver(@ApplicationContext applicationContext: Context): ContentResolver {
        return applicationContext.contentResolver
    }
}
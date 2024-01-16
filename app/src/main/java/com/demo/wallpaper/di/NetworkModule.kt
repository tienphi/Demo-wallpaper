package com.demo.wallpaper.di

import com.demo.wallpaper.data.network.api.UnsplashService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideUnsplashService(): UnsplashService {
        return UnsplashService.create()
    }
}

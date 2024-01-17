package com.demo.wallpaper.di

import com.demo.wallpaper.data.network.api.ImageService
import com.demo.wallpaper.data.network.api.ImageService.Companion.GIPHY_BASE_URL
import com.demo.wallpaper.data.network.api.ImageService.Companion.UNSPLASH_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @UnsplashImageService
    @Provides
    fun provideUnsplashService(): ImageService {
        return ImageService.create(UNSPLASH_BASE_URL)
    }

    @Singleton
    @GiphyGifService
    @Provides
    fun provideGiphyGifService(): ImageService {
        return ImageService.create(GIPHY_BASE_URL)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnsplashImageService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GiphyGifService

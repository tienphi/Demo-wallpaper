package com.demo.wallpaper.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.demo.wallpaper.data.model.GiphyGif
import com.demo.wallpaper.data.model.UnsplashPhoto
import com.demo.wallpaper.data.network.api.ImageService
import com.demo.wallpaper.data.network.model.asExternalModel
import com.demo.wallpaper.di.GiphyGifService
import com.demo.wallpaper.di.UnsplashImageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepository @Inject constructor(
    @UnsplashImageService private val unSplashService: ImageService,
    @GiphyGifService private val giphyService: ImageService
) {

    fun getImageSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(unSplashService, query) }
        ).flow
            .map { pagingData ->
                pagingData.map { it.asExternalModel() }
            }
    }

    fun getGifSearchResultStream(query: String): Flow<PagingData<GiphyGif>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { GiphyPagingDataSource(giphyService, query) }
        ).flow
            .map { pagingData ->
                pagingData.map { it.asExternalModel() }
            }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}

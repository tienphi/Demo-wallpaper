package com.demo.wallpaper.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.demo.wallpaper.data.model.UnsplashPhoto
import com.demo.wallpaper.data.network.api.UnsplashService
import com.demo.wallpaper.data.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val service: UnsplashService) {

    fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(service, query) }
        ).flow
            .map { pagingData ->
                pagingData.map { it.asExternalModel() }
            }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}

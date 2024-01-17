package com.demo.wallpaper.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.demo.wallpaper.data.network.api.ImageService
import com.demo.wallpaper.data.network.model.NetworkGiphyGif

private const val GIPHY_STARTING_PAGE_INDEX = 1
private const val OFFSET_MAX = 4999
class GiphyPagingDataSource(
    private val service: ImageService,
    private val query: String
) : PagingSource<Int, NetworkGiphyGif>(){
    override fun getRefreshKey(state: PagingState<Int, NetworkGiphyGif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkGiphyGif> {
        val page = params.key ?: GIPHY_STARTING_PAGE_INDEX
        return try {
            val offset: Int = if (page > 0)
                params.loadSize * (page - 1) else 0

            val response = service.searchGifs(
                query = query,
                limit = params.loadSize,
                offset = offset)
            val gifs = response.data
            LoadResult.Page(
                data = gifs,
                prevKey = if (page == GIPHY_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (offset == OFFSET_MAX || offset == response.pagination.totalCount - 1) null
                else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}
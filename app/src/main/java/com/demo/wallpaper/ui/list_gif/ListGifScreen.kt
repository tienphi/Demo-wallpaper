package com.demo.wallpaper.ui.list_gif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.demo.wallpaper.data.model.GiphyGif
import com.demo.wallpaper.ui.list_gif.component.GifItem
import kotlinx.coroutines.flow.Flow

@Composable
fun ListGifScreen(
    viewModel: ListGifViewModel = hiltViewModel(),
    onGifClick: (GiphyGif) -> Unit,
) {
    ListGifScreen(
        pictures = viewModel.gifs,
        title = "Animals",
        onGifClick = onGifClick,
        onPullToRefresh = viewModel::refreshData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListGifScreen(
    pictures: Flow<PagingData<GiphyGif>>,
    title: String,
    onGifClick: (GiphyGif) -> Unit,
    onPullToRefresh: () -> Unit,
) {
    Scaffold(
        topBar = {
            ListGifTopBar(title = title)
        }
    ) { padding ->
        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            onPullToRefresh()
        }

        val pagingItems: LazyPagingItems<GiphyGif> =
            pictures.collectAsLazyPagingItems()

        LaunchedEffect(pagingItems.loadState) {
            when (pagingItems.loadState.refresh) {
                is LoadState.Loading -> Unit
                is LoadState.Error, is LoadState.NotLoading -> {
                    pullToRefreshState.endRefresh()
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    val photo = pagingItems[index] ?: return@items

                    GifItem(url = photo.images.original.url)
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListGifTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = "Gifs by Giphy - $title") },
        modifier = modifier,
    )
}

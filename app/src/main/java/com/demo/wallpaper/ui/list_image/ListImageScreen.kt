package com.demo.wallpaper.ui.list_image

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
import com.demo.wallpaper.data.model.UnsplashPhoto
import com.demo.wallpaper.ui.list_image.component.ImageItem
import kotlinx.coroutines.flow.Flow

@Composable
fun ListImageScreen(
    viewModel: ListImageViewModel = hiltViewModel(),
    onPhotoClick: (String) -> Unit,
) {
    ListImageScreen(
        pictures = viewModel.pictures,
        title = "Weather",
        onPhotoClick = onPhotoClick,
        onPullToRefresh = viewModel::refreshData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListImageTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = "Photos by Unsplash - $title") },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListImageScreen(
    pictures: Flow<PagingData<UnsplashPhoto>>,
    title: String,
    onPhotoClick: (String) -> Unit,
    onPullToRefresh: () -> Unit,
) {
    Scaffold(
        topBar = {
            ListImageTopBar(title = title)
        }
    ) { padding ->
        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            onPullToRefresh()
        }

        val pagingItems: LazyPagingItems<UnsplashPhoto> =
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

                    ImageItem(
                        url = photo.urls.small,
                        onClickItem = { onPhotoClick(photo.urls.full) }
                    )
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState
            )
        }
    }
}

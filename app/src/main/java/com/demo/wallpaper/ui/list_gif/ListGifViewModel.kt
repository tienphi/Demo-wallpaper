package com.demo.wallpaper.ui.list_gif

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.demo.wallpaper.data.ImageRepository
import com.demo.wallpaper.data.model.GiphyGif
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListGifViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ImageRepository
) : ViewModel() {

    private var queryString: String? = savedStateHandle["keyword"]

    private val _gifs = MutableStateFlow<PagingData<GiphyGif>?>(null)
    val gifs: Flow<PagingData<GiphyGif>> get() = _gifs.filterNotNull()

    init {
        refreshData()
    }

    fun refreshData() {

        viewModelScope.launch {
            try {
                _gifs.value =
                    repository.getGifSearchResultStream(queryString ?: "Animals")
                        .cachedIn(viewModelScope)
                        .first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

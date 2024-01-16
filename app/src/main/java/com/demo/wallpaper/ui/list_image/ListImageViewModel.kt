package com.demo.wallpaper.ui.list_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.demo.wallpaper.data.UnsplashRepository
import com.demo.wallpaper.data.network.model.UnsplashPhotoNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UnsplashRepository
) : ViewModel() {

    private var queryString: String? = savedStateHandle["plantName"]

    private val _pictures = MutableStateFlow<PagingData<UnsplashPhotoNetwork>?>(null)
    val pictures: Flow<PagingData<UnsplashPhotoNetwork>> get() = _pictures.filterNotNull()

    init {
        refreshData()
    }

    fun refreshData() {

        viewModelScope.launch {
            try {
                _pictures.value =
                    repository.getSearchResultStream(queryString ?: "Animals").cachedIn(viewModelScope)
                        .first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

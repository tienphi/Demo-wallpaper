package com.demo.wallpaper.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

const val TYPE_IMAGE = "image"
const val TYPE_GIF = "gif"

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var url: String? = savedStateHandle["url"]
    private var type: String? = savedStateHandle["type"]

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> get() = _uiState

    init {
        val data: String = url
            ?: "https://media1.giphy.com/media/33zX3zllJBGY8/giphy.gif?cid=9eb124f18qbdxco9da2u9h43aawsudud3wsdwiffnlz0xxhd&ep=v1_gifs_search&rid=giphy.gif&ct=g"

        val imageType: String = type ?: TYPE_GIF
        _uiState.value = when (
            data
        ) {
            "" -> DetailUiState.Error(msg = "Url wrong")
            else -> DetailUiState.Success(url = data, type = imageType)
        }
    }
}

sealed interface DetailUiState {
    data class Success(val url: String, val type: String) : DetailUiState
    data class Error(val msg: String) : DetailUiState
    data object Loading : DetailUiState
}

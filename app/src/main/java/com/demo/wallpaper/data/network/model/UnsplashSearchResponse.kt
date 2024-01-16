package com.demo.wallpaper.data.network.model

import com.google.gson.annotations.SerializedName

data class UnsplashSearchResponse (
    @field:SerializedName("results") val results: List<UnsplashPhotoNetwork>,
    @field:SerializedName("total_pages") val totalPages: Int
)

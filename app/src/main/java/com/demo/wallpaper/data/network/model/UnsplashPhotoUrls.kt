package com.demo.wallpaper.data.network.model

import com.google.gson.annotations.SerializedName

data class UnsplashPhotoUrls(
    @field:SerializedName("small") val small: String,
    @field:SerializedName("full") val full: String,
    @field:SerializedName("regular") val regular: String,
    @field:SerializedName("raw") val raw: String,
    @field:SerializedName("thumb") val thumb: String,
)

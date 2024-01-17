package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.UnsplashPhotoUrls
import com.google.gson.annotations.SerializedName

data class NetworkUnsplashPhotoUrls(
    @field:SerializedName("small") val small: String,
    @field:SerializedName("full") val full: String,
    @field:SerializedName("regular") val regular: String,
    @field:SerializedName("raw") val raw: String,
    @field:SerializedName("thumb") val thumb: String,
)

fun NetworkUnsplashPhotoUrls.asExternalModel() = UnsplashPhotoUrls(
    small = small,
    full = full,
    regular = regular,
    raw = raw,
    thumb = thumb
)

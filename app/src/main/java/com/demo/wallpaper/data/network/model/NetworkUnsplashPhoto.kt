package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.UnsplashPhoto
import com.google.gson.annotations.SerializedName

data class NetworkUnsplashPhoto(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("urls") val urls: NetworkUnsplashPhotoUrls,
    @field:SerializedName("user") val user: NetworkUnsplashUser
)

fun NetworkUnsplashPhoto.asExternalModel() = UnsplashPhoto(
    id = id,
    urls = urls.asExternalModel(),
    user = user.asExternalModel()
)
